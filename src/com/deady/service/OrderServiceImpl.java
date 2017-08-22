package com.deady.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deady.dao.ItemDAO;
import com.deady.dao.OrderDAO;
import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;
import com.deady.entity.client.Client;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;
import com.deady.entity.store.Store;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;
import com.deady.utils.printer.ORDERSIDE;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private ClientService clientService;
	@Autowired
	private StoreService storeService;

	private static final String SUFFIX = " ";

	@Override
	public void addOrder(Order order) {
		orderDAO.insertOrder(order);
	}

	@Override
	public OrderDto getOrderDtoById(String orderId) {
		Order order = orderDAO.findOrderById(orderId);
		OrderDto dto = new OrderDto(order);
		List<Item> itemList = itemDAO.findItemsByOrderId(orderId);
		dto.setItemList(itemList);
		return dto;
	}

	@Override
	public List<OrderDto> getOrderDtoByCondition(OrderSearchEntity orderSearch) {
		List<OrderDto> orderList = orderDAO.findOrderByCondition(orderSearch);
		List<Map<String, String>> cusId2NameMap = clientService
				.getClientId2NameMap(orderSearch.getStoreId());
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Map<String, String> map : cusId2NameMap) {
			String name = null;
			String id = null;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if ("id".equals(entry.getKey())) {
					id = entry.getValue();
				} else if ("name".equals(entry.getKey())) {
					name = entry.getValue();
				}
			}
			resultMap.put(id, name);
		}
		String _cusName = orderSearch.getCusName();
		boolean needCheckCusName = false;
		if (!StringUtils.isEmpty(_cusName)) {
			needCheckCusName = true;
		}
		List<OrderDto> resultList = new ArrayList<OrderDto>();
		for (Order order : orderList) {
			OrderDto dto = new OrderDto(order);
			String cusName = resultMap.get(dto.getCusId());
			if (needCheckCusName && cusName.indexOf(_cusName) == -1) {
				continue;
			}
			dto.setItemList(itemDAO.findItemsByOrderId(order.getId()));
			dto.setCusName(StringUtils.isEmpty(cusName) ? "" : cusName);
			resultList.add(dto);
		}
		return resultList;
	}

	/**
	 * 打印订单
	 */
	@Override
	public void printOrder(String orderId, Operator op) throws Exception {
		OrderDto dto = getOrderDtoById(orderId);
		Store store = storeService.getStoreById(op.getStoreId());
		Client client = clientService.getClientById(dto.getCusId());
		Device device = null;
		try {
			device = new Device();
			DeviceParameters params = new DeviceParameters();
			device.setDeviceParameters(params);
			device.openDevice();
			Date currentTime = new Date();
			// 打印店铺联
			printOrder(device, store, op, client, dto, ORDERSIDE.STORE_SIDE,
					currentTime);
			device.printString("");
			device.printString("");
			// // 打印客户联
			printOrder(device, store, op, client, dto, ORDERSIDE.CUSTOMER_SIDE,
					currentTime);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 关闭设备 释放内存
			device.closeDevice();
		}

	}

	private void printOrder(Device device, Store store, Operator op,
			Client client, OrderDto dto, ORDERSIDE storeSide, Date currentTime) {

		// TODO logo
		// device.selectAlignment(ALIGNMENT.CENTER);// 居中
		// device.setImageParameters(BITDEPTH.GRAYSCALE,
		// IMAGEPROCESSING.SHARPENING,
		// 0);
		// device.scanCheck(true, 200);
		// byte[] receivedBytes = device.getImageDataBytes();
		// device.ejectCheck();
		// device.selectImageFormat(IMAGEFORMAT.JPEG_HIGH);

		device.selectAlignType(2);// 右对齐
		switch (storeSide.getSide()) {
		case 1:
			device.printString("店铺联");
			break;
		case 2:
			device.printString("客户联");
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		device.selectAlignType(0);// 左对齐
		device.printString("店名:" + store.getName());
		device.printString("地址:" + store.getAddress());
		device.printString("电话:" + store.getTelePhone());
		device.printString("手机:" + store.getMobilePhone());
		device.printString("------------------------------------------------");
		device.printString("交易号:" + dto.getId());
		device.printString("收款员:" + op.getId());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		device.printString("日期:" + dateString);
		device.printString("客户名称:" + client.getName() + "    客户电话:"
				+ client.getPhone());
		device.printString("================================================");
		String title = paddingWithSuffix(16, "商品名称", SUFFIX)
				+ paddingWithSuffix(6, "尺寸", SUFFIX)
				+ paddingWithSuffix(10, "单价(元)", SUFFIX)
				+ paddingWithSuffix(6, "数量", SUFFIX)
				+ paddingWithSuffix(10, "金额(元)", SUFFIX);
		device.printString(title);
		List<Item> itemList = dto.getItemList();
		for (Item item : itemList) {
			device.printString(paddingWithSuffix(16, item.getName(), SUFFIX)
					+ paddingWithSuffix(6, item.getSize(), SUFFIX)
					+ paddingWithSuffix(10, item.getUnitPrice(), SUFFIX)
					+ paddingWithSuffix(6, item.getAmount(), SUFFIX)
					+ paddingWithSuffix(10, item.getPrice(), SUFFIX));
		}
		device.printString("------------------------------------------------");
		device.selectAlignType(2);// 右对齐
		device.printString("小计:" + dto.getSmallCount() + "元");
		device.printString("折扣金额:" + dto.getDiscount() + "元");
		device.printString("应付金额:" + dto.getTotalAmount() + "元");
		device.printString("备注:"
				+ (StringUtils.isEmpty(dto.getRemark()) ? "" : dto.getRemark()));
		switch (storeSide.getSide()) {
		case 1:

			break;
		case 2:
			// 打印店铺信息
			device.printString("温馨提示:" + store.getReminder());
			// String qrcode = paddingWithSuffix(16, "微信付款", SUFFIX)
			// + paddingWithSuffix(16, "支付宝付款", SUFFIX)
			// + paddingWithSuffix(16, "微信加好友", SUFFIX);
			// device.printString(qrcode);
			// TODO 打印二维码
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		// 裁剪纸张
		device.cutPaper();

	}

	/**
	 * 
	 * @param length
	 *            限定字符串长度
	 * @param text
	 *            输入的字符
	 * @param suffix
	 *            如果长度不足,用suffix来补足,如果超了就截取指定长度
	 * @return
	 */
	private String paddingWithSuffix(int length, String text, String suffix) {
		// 获取text的按照Byte计算的实际长度 一个中文两个字符 英文数字一个字符
		int realLength = getChineseLength(text);
		StringBuffer sb = new StringBuffer(text);
		if (realLength < length) {
			for (int i = 0; i < (length - realLength); i++) {
				sb.append(SUFFIX);
			}
		} else if (realLength > length) {// 太长就截掉
			StringBuffer tempSb = new StringBuffer();
			String chinese = "[\u0391-\uFFE5]";
			int valueLength = 0;
			for (int i = 0; i < text.length(); i++) {
				String temp = text.substring(i, i + 1);
				if (temp.matches(chinese)) {
					valueLength += 2;
				} else {
					valueLength += 1;
				}
				if (valueLength <= length) {
					tempSb.append(temp);
				} else {
					break;
				}
			}
			return paddingWithSuffix(length, tempSb.toString(), suffix);
		}
		return sb.toString();
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param validateStr
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int getChineseLength(String validateStr) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < validateStr.length(); i++) {
			/* 获取一个字符 */
			String temp = validateStr.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	@Override
	public void removeOrder(String orderId) {
		orderDAO.deleteOrderById(orderId);
		itemDAO.deleItemsByOrderId(orderId);
	}

}
