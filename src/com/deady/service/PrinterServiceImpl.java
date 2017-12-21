package com.deady.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.client.Client;
import com.deady.entity.store.Store;
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

}
