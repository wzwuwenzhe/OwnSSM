package com.deady.action.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;
import com.deady.entity.client.Client;
import com.deady.entity.operator.Operator;
import com.deady.entity.store.Store;
import com.deady.service.ClientService;
import com.deady.service.ItemService;
import com.deady.service.OrderService;
import com.deady.service.StoreService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;
import com.deady.utils.printer.ORDERSIDE;
import com.deady.utils.printer.Printer;
import com.epson.EpsonCom.EpsonCom.ALIGNMENT;
import com.epson.EpsonCom.EpsonCom.BITDEPTH;
import com.epson.EpsonCom.EpsonCom.IMAGEFORMAT;
import com.epson.EpsonCom.EpsonCom.IMAGEPROCESSING;
import com.epson.EpsonCom.EpsonCom.PAPERSIDE;
import com.epson.EpsonCom.EpsonCom.PRINTDIRECTION;

@Controller
public class BillingAction {

	@Autowired
	private ClientService clientService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreService storeService;

	@RequestMapping(value = "/billing", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object billing(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Client> clientList = clientService.getClientListByStoreId(op
				.getStoreId());
		req.setAttribute("clientList", clientList);
		return new ModelAndView("/bill/billing");
	}

	@RequestMapping(value = "/billing", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doBilling(HttpServletRequest req, HttpServletResponse res,
			String[] name, String[] size, String[] unitPrice, String[] amount,
			String[] price) throws Exception {
		FormResponse response = new FormResponse(req);
		if (StringUtils.isEmpty(req.getParameter("cusId"))) {
			response.setSuccess(false);
			response.setMessage("请先选择客户");
			return response;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		Order order = new Order();
		ActionUtil.assObjByRequest(req, order);
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(now);
		String orderId = dateString
				+ UUID.randomUUID().toString().replaceAll("-", "")
						.substring(0, 6).toUpperCase();
		order.setId(orderId);
		order.setOperatorId(op.getId());
		order.setStoreId(op.getStoreId());
		List<Item> itemList = new ArrayList<Item>();
		if (null == name || name.length <= 0 || StringUtils.isEmpty(name)) {
			response.setSuccess(false);
			response.setMessage("请填写订单内容");
			return response;
		}
		for (int i = 0; i < name.length; i++) {
			// 后期考虑加验证 每一项都需要验证
			Item item = new Item();
			item.setName(name[i]);
			item.setSize(size[i]);
			item.setUnitPrice(unitPrice[i]);
			item.setAmount(amount[i]);
			item.setPrice(price[i]);
			item.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			item.setOrderId(orderId);
			itemList.add(item);
		}
		itemService.addItem(itemList);
		orderService.addOrder(order);
		// 发送打印订单请求
		try {
			printOrder(orderId, op);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
		response.setSuccess(true);
		response.setMessage("订单添加成功!");
		response.setData("/index");
		return response;
	}

	/**
	 * 打印订单
	 */
	private void printOrder(String orderId, Operator op) throws Exception {
		OrderDto dto = orderService.getOrderDtoById(orderId);
		Store store = storeService.getStoreById(op.getStoreId());
		Client client = clientService.getClientById(dto.getCusId());
		Printer p = null;
		try {
			p = new Printer("192.168.31.101", 9100);
			p.selectPrintDirection(PRINTDIRECTION.LEFTTORIGHT);
			// 打印店铺联
			printStoreSide(p, store, op, client, dto, ORDERSIDE.STORE_SIDE);
			// 打印客户联
			printStoreSide(p, store, op, client, dto, ORDERSIDE.CUSTOMER_SIDE);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 关闭设备 释放内存
			p.closeDevice();
		}

	}

	private void printStoreSide(Printer p, Store store, Operator op,
			Client client, OrderDto dto, ORDERSIDE storeSide) {

		// TODO logo
		// p.selectAlignment(ALIGNMENT.CENTER);// 居中
		// p.setImageParameters(BITDEPTH.GRAYSCALE, IMAGEPROCESSING.SHARPENING,
		// 0);
		// p.scanCheck(true, 200);
		// byte[] receivedBytes = p.getImageDataBytes();
		// p.ejectCheck();
		// p.selectImageFormat(IMAGEFORMAT.JPEG_HIGH);

		p.selectSlipPaper(PAPERSIDE.FRONT);// 空一行
		p.selectAlignment(ALIGNMENT.RIGHT);// 右对齐
		switch (storeSide.getSide()) {
		case 1:
			p.print("店铺联");
			break;
		case 2:
			p.print("客户联");
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		p.selectAlignment(ALIGNMENT.LEFT);// 左对齐
		p.print("店名:" + store.getName());
		p.print("地址:" + store.getAddress());
		p.print("电话:" + store.getTelePhone());
		p.print("手机:" + store.getMobilePhone());
		p.print("-----------------------------------------------");
		p.selectSlipPaper(PAPERSIDE.FRONT);// 空一行
		p.print("交易号:" + store.getMobilePhone());
		p.print("收款员:" + op.getId());
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		p.print("日期:" + dateString);
		p.print("客户名称:" + client.getName() + "    客户电话:" + client.getPhone());
		p.print("===============================================");
		p.print("商品名称    尺寸    单价    数量    金额");
		List<Item> itemList = dto.getItemList();
		for (Item item : itemList) {
			p.print(item.getName() + "  " + item.getSize() + "  "
					+ item.getUnitPrice() + "  " + item.getAmount() + "  "
					+ item.getPrice());
		}
		p.print("-----------------------------------------------");
		p.print("小计:" + dto.getSmallCount() + "元");
		p.print("折扣金额:" + dto.getDiscount() + "元");
		p.print("应付金额:" + dto.getTotalAmount() + "元");
		p.print("备注:" + dto.getRemark());
		switch (storeSide.getSide()) {
		case 1:

			break;
		case 2:
			// 打印店铺信息
			p.print("温馨提示:" + dto.getRemark());
			p.selectAlignment(ALIGNMENT.CENTER);
			p.print("微信付款      支付宝付款      微信加好友");
			// TODO 打印二维码
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		// 裁剪纸张
		p.cutPaper();

	}

}
