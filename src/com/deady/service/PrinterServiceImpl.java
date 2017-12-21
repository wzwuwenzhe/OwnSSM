package com.deady.service;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;
import com.deady.enums.OrderStateEnum;
import com.deady.enums.PayTypeEnum;
import com.deady.printer.Device;
import com.deady.utils.DateUtils;
import com.deady.utils.printer.ORDERSIDE;

@Service
public class PrinterServiceImpl implements PrinterService {

	private static final String SUFFIX = " ";

	@Override
	public void printOrder(Device device, Store store, Client client,
			OrderDto dto, ORDERSIDE storeSide, Date currentTime,
			boolean isRePrint) {

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
			if (isRePrint) {
				device.printString("店铺联(重新打印!)");
			} else {
				device.printString("店铺联");
			}
			break;
		case 2:
			if (isRePrint) {
				device.printString("客户联(重新打印!)");
			} else {
				device.printString("客户联");
			}
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		device.selectAlignType(0);// 左对齐
		if (storeSide.getSide() == 2) {
			device.selectFontSize(68);
			device.printString(store.getName());
			device.selectFontSize(0);
			device.printString("");
			device.printString("");
			device.printString("地址:" + store.getAddress());
			device.printString("电话:" + store.getTelePhone());
			device.printString("手机:" + store.getMobilePhone());
			device.printString("------------------------------------------------");
		}
		device.printString("交易号:" + dto.getId());
		device.printString("收款员:" + dto.getOperatorId());
		Date creatTime = DateUtils.convert2Date(dto.getCreationTime(),
				"yyyy-MM-dd HH:mm:ss");
		String dateString = DateUtils.convert2String(creatTime,
				"yyyy-MM-dd HH:mm:ss");
		device.printString("日期:" + dateString);
		if (storeSide.getSide() == 1) {
			device.selectFontSize(17);
		}
		device.printString("客户名称:" + client.getName());
		device.selectFontSize(0);
		device.printString("================================================");
		String title = Device.paddingWithSuffix(10, "款号", SUFFIX)
				+ Device.paddingWithSuffix(8, "颜色", SUFFIX)
				+ Device.paddingWithSuffix(8, "尺码", SUFFIX)
				+ Device.paddingWithSuffix(6, "数量", SUFFIX)
				+ Device.paddingWithSuffix(8, "单价", SUFFIX)
				+ Device.paddingWithSuffix(8, "金额", SUFFIX);
		device.printString(title);
		List<Item> itemList = dto.getItemList();

		if (storeSide.getSide() == 1) {
			device.selectFontSize(17);
		}
		for (Item item : itemList) {
			device.printString(Device.paddingWithSuffix(10, item.getName(),
					SUFFIX)
					+ Device.paddingWithSuffix(8, item.getColor(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getSize(), SUFFIX)
					+ Device.paddingWithSuffix(6, item.getAmount(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getUnitPrice(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getPrice(), SUFFIX));
			if (storeSide.getSide() == 1) {
				device.selectFontSize(0);
				device.printString("------------------------------------------------");
				device.selectFontSize(17);
			}
		}
		device.selectFontSize(0);
		device.printString("");
		device.printString("");
		device.printString("================================================");
		device.selectAlignType(2);// 右对齐
		device.printString("小计:" + dto.getSmallCount() + "元");
		// device.printString("折扣金额:" + dto.getDiscount() + "元");
		device.printString("应付金额:" + dto.getTotalAmount() + "元");
		device.printString("付款方式:  "
				+ PayTypeEnum.typeOf(Integer.parseInt(dto.getPayType()))
						.getPayTypeInfo());
		if (storeSide.getSide() == 1) {
			device.selectFontSize(17);
		}
		device.printString("送货地址:"
				+ (StringUtils.isEmpty(dto.getAddress()) ? "" : dto
						.getAddress()));
		device.printString("备注:"
				+ (StringUtils.isEmpty(dto.getRemark()) ? "" : dto.getRemark()));
		device.selectFontSize(0);
		switch (storeSide.getSide()) {
		case 1:
			device.printString("");
			device.printString("");
			device.printString("");
			device.printString("");
			break;
		case 2:
			// 打印店铺信息
			device.printString("温馨提示:" + store.getReminder());
			// String qrcode = Device.paddingWithSuffix(16, "微信付款", SUFFIX)
			// + Device.paddingWithSuffix(16, "支付宝付款", SUFFIX)
			// + Device.paddingWithSuffix(16, "微信加好友", SUFFIX);
			// device.printString(qrcode);
			// TODO 打印二维码
			device.printString("");
			device.printString("");
			break;
		default:
			throw new RuntimeException("未知票联");
		}
		// 裁剪纸张
		device.cutPaper();

	}

	@Override
	public void printRecordForOneDay(Device device, String dateStr,
			List<OrderDto> records) throws Exception {
		device.printString(dateStr.substring(0, 4) + "年"
				+ dateStr.substring(4, 6) + "月" + dateStr.substring(6, 8)
				+ "日 报表");
		double cash = 0;
		double card = 0;
		double zfb = 0;
		double weixin = 0;
		double monthPay = 0;
		double dayTotalPrice = 0;
		// 款号和单价对应数量的散列表
		// 对map的key进行排序
		Map<String, Integer> nameAndPrice2AmountMap = new TreeMap<String, Integer>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});

		for (OrderDto record : records) {
			// 过滤未付款订单
			if (record.getPayType().equals(PayTypeEnum.NOTPAY.getType() + "")) {
				continue;
			}
			if (record.getPayType().equals(PayTypeEnum.CASH.getType() + "")) {
				cash += Double.parseDouble(record.getTotalAmount());
			}
			if (record.getPayType().equals(PayTypeEnum.CARD.getType() + "")) {
				card += Double.parseDouble(record.getTotalAmount());
			}
			if (record.getPayType().equals(PayTypeEnum.ZFB.getType() + "")) {
				zfb += Double.parseDouble(record.getTotalAmount());
			}
			if (record.getPayType().equals(PayTypeEnum.WEIXIN.getType() + "")) {
				weixin += Double.parseDouble(record.getTotalAmount());
			}
			if (record.getPayType().equals(PayTypeEnum.MONTHPAY.getType() + "")) {
				monthPay += Double.parseDouble(record.getTotalAmount());
			}
			dayTotalPrice += Double.parseDouble(record.getTotalAmount());
			// 打印记录
			printRecord(device, record, nameAndPrice2AmountMap);
		}
		device.selectAlignType(1);// 居中

		for (Map.Entry<String, Integer> entry : nameAndPrice2AmountMap
				.entrySet()) {
			String[] nameAndPriceArr = entry.getKey().split(",");
			Integer amount = entry.getValue();
			String name = nameAndPriceArr[0];
			String price = nameAndPriceArr[1];
			device.printString("当天款式: " + name + " 单价: " + price + " 共卖出: "
					+ amount + "件");
		}
		device.printString("当天现金:" + cash + "元");
		device.printString("当天刷卡:" + card + "元");
		device.printString("当天支付宝:" + zfb + "元");
		device.printString("当天微信:" + weixin + "元");
		device.printString("当天月结:" + monthPay + "元");
		device.printString("当天销售额:" + dayTotalPrice + "元");
		device.printString("");
		device.printString("");
		device.cutPaper();
	}

	private void printRecord(Device device, OrderDto record,
			Map<String, Integer> nameAndPrice2AmountMap) {
		device.selectAlignType(0);// 左对齐
		device.printString("客户名称:" + record.getCusName());
		device.printString("------------------------------------------------");
		String title = Device.paddingWithSuffix(10, "款号", SUFFIX)
				+ Device.paddingWithSuffix(8, "颜色", SUFFIX)
				+ Device.paddingWithSuffix(8, "尺码", SUFFIX)
				+ Device.paddingWithSuffix(8, "数量", SUFFIX)
				+ Device.paddingWithSuffix(6, "单价", SUFFIX)
				+ Device.paddingWithSuffix(8, "金额", SUFFIX);
		device.printString(title);
		List<Item> itemList = record.getItemList();
		for (Item item : itemList) {
			device.printString(Device.paddingWithSuffix(10, item.getName(),
					SUFFIX)
					+ Device.paddingWithSuffix(8, item.getColor(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getSize(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getAmount(), SUFFIX)
					+ Device.paddingWithSuffix(6, item.getUnitPrice(), SUFFIX)
					+ Device.paddingWithSuffix(8, item.getPrice(), SUFFIX));
			// 统计款式售出数量
			String name = item.getName();
			String price = item.getUnitPrice();
			String key = name + "," + price;
			String amount = item.getAmount();
			if (null == nameAndPrice2AmountMap.get(key)) {
				nameAndPrice2AmountMap.put(key, Integer.parseInt(amount));
			} else {
				int lastAmount = nameAndPrice2AmountMap.get(key);
				lastAmount += Integer.parseInt(amount);
				nameAndPrice2AmountMap.put(key, lastAmount);
			}
		}
		device.printString("------------------------------------------------");
		device.selectAlignType(2);// 右对齐
		device.printString(StringUtils.isEmpty(record.getRemark()) ? ""
				: record.getRemark());
		device.printString("发货状态:"
				+ OrderStateEnum.typeOf(Integer.parseInt(record.getState()))
						.getOrderStateInfo()
				+ "  付款方式:"
				+ PayTypeEnum.typeOf(Integer.parseInt(record.getPayType()))
						.getPayTypeInfo() + "  金额:" + record.getTotalAmount()
				+ "元");
		device.printString("================================================");
	}

}
