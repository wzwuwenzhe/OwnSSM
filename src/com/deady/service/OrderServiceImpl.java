package com.deady.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.dao.ItemDAO;
import com.deady.dao.OrderDAO;
import com.deady.dao.StockDAO;
import com.deady.dto.OrderDto;
import com.deady.entity.bill.Item;
import com.deady.entity.bill.Order;
import com.deady.entity.client.Client;
import com.deady.entity.operator.Operator;
import com.deady.entity.order.OrderSearchEntity;
import com.deady.entity.stock.Storage;
import com.deady.entity.store.Store;
import com.deady.enums.OrderStateEnum;
import com.deady.enums.PayTypeEnum;
import com.deady.printer.Device;
import com.deady.printer.DeviceParameters;
import com.deady.utils.ActionUtil;
import com.deady.utils.DateUtils;
import com.deady.utils.HttpClientUtil;
import com.deady.utils.OperatorSessionInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	@Autowired
	private StockDAO stockDAO;

	private static PropertiesConfiguration config = ConfigUtil
			.getProperties("deady");
	private static Logger logger = LoggerFactory
			.getLogger(OrderServiceImpl.class);

	private static final String SUFFIX = " ";

	@Override
	public void addOrder(Order order) {
		orderDAO.insertOrder(order);
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrderDto getOrderDtoById(String orderId) {
		Order order = orderDAO.findOrderById(orderId);
		String cusId = order.getCusId();
		Client client = clientService.getClientById(cusId);
		OrderDto dto = new OrderDto(order);
		dto.setCusName(client.getName());
		List<Item> itemList = itemDAO.findItemsByOrderId(orderId);
		dto.setItemList(itemList);
		return dto;
	}

	@SuppressWarnings("unchecked")
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
		for (OrderDto order : orderList) {
			OrderDto dto = new OrderDto(order);
			dto.setCreationTime(DateUtils.convert2String(DateUtils
					.convert2Date(order.getCreationTime(),
							"yyyy-MM-dd HH:mm:ss.s"), "yyyyMMdd"));
			String cusName = resultMap.get(dto.getCusId());
			if (needCheckCusName && cusName.indexOf(_cusName) == -1) {
				continue;
			}
			List<Item> tempList = itemDAO.findItemsByOrderId(order.getId());
			List<Item> returnTempList = itemDAO.findReturnItemsByOrderId(order
					.getReturnOrderId());
			dto.setItemList(tempList);
			dto.setReturnItemList(returnTempList);
			dto.setCusName(StringUtils.isEmpty(cusName) ? "" : cusName);
			resultList.add(dto);
		}
		return resultList;
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
	private static String paddingWithSuffix(int length, String text,
			String suffix) {
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
		Order order = orderDAO.findOrderById(orderId);
		List<Item> items = itemDAO.findItemsByOrderId(orderId);
		Map<String, Integer> name2AmountMap = new HashMap<String, Integer>();
		if (null != items && items.size() > 0) {
			for (Item item : items) {
				String name = item.getName();// 款号
				int amount = Integer.parseInt(item.getAmount());
				Integer lastAmount = name2AmountMap.get(name);
				if (null == lastAmount) {
					name2AmountMap.put(name, amount);
				} else {
					name2AmountMap.put(name, lastAmount + amount);
				}
			}
		}
		for (Map.Entry<String, Integer> entry : name2AmountMap.entrySet()) {
			String name = entry.getKey();
			int amount = entry.getValue();
			String year = ActionUtil.getLunarCalendarYear();
			Storage storage = stockDAO.findStorageByNameAndStoreId(year, name,
					order.getStoreId());
			if (null == storage) {// 找不到这个款式
				continue;
			}
			storage.setStockLeft(Integer.parseInt(storage.getStockLeft())
					+ amount + "");
			stockDAO.updateStorage(storage);
		}
		// 把状态标记为9 不去真正的删除它
		orderDAO.deleteOrderById(orderId);
		// itemDAO.deleItemsByOrderId(orderId);
	}

	@Override
	public boolean printReport(String startDateStr, String endDateStr,
			HttpServletRequest req) throws UnsupportedEncodingException {
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		ActionUtil.assObjByRequest(req, orderSearch);
		orderSearch.setBeginDate(startDateStr);
		orderSearch.setEndDate(endDateStr);
		// 将结束时间自动加一天
		Date nextDate = DateUtils
				.addDays(DateUtils.convert2Date(orderSearch.getEndDate(),
						"yyyyMMdd"), 1);
		orderSearch.setEndDate(DateUtils.convert2String(nextDate, "yyyyMMdd"));
		Operator operator = OperatorSessionInfo.getOperator(req);
		// 如果有传storeId值进来 就说明是管理员身份
		if (StringUtils.isEmpty(orderSearch.getStoreId())) {
			orderSearch.setStoreId(operator.getStoreId());
		}
		List<OrderDto> orderList = getOrderDtoByCondition(orderSearch);
		if (orderList.size() == 0) {
			return true;
		}
		// 组装数据
		Map<String, List<OrderDto>> day2recordMap = new LinkedHashMap<String, List<OrderDto>>();
		for (OrderDto orderDto : orderList) {
			List<OrderDto> records = day2recordMap.get(orderDto
					.getCreationTime());
			if (null == records || records.size() == 0) {
				records = new ArrayList<OrderDto>();
				day2recordMap.put(orderDto.getCreationTime(), records);
			}
			records.add(orderDto);
		}
		// 开始打印
		for (Map.Entry<String, List<OrderDto>> entry : day2recordMap.entrySet()) {
			String dateStr = entry.getKey();
			List<OrderDto> records = entry.getValue();
			printRecordForOneDay(dateStr, records);
		}

		return false;
	}

	/**
	 * 打印某一天的报表
	 */
	private void printRecordForOneDay(String dateStr, List<OrderDto> records) {
		Device device = null;
		try {
			device = new Device();
			DeviceParameters params = new DeviceParameters();
			device.setDeviceParameters(params);
			device.openDevice();
			// 打印店铺联
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
			Map<String, Integer> nameAndPrice2AmountMap = new HashMap<String, Integer>();
			for (OrderDto record : records) {
				// 过滤未付款订单
				if (record.getPayType().equals(
						PayTypeEnum.NOTPAY.getType() + "")) {
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
				if (record.getPayType().equals(
						PayTypeEnum.WEIXIN.getType() + "")) {
					weixin += Double.parseDouble(record.getTotalAmount());
				}
				if (record.getPayType().equals(
						PayTypeEnum.MONTHPAY.getType() + "")) {
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// 关闭设备 释放内存
			device.closeDevice();
		}

	}

	private void printRecord(Device device, OrderDto record,
			Map<String, Integer> nameAndPrice2AmountMap) {
		device.selectAlignType(0);// 左对齐
		device.printString("客户名称:" + record.getCusName());
		device.printString("------------------------------------------------");
		String title = paddingWithSuffix(10, "款号", SUFFIX)
				+ paddingWithSuffix(8, "颜色", SUFFIX)
				+ paddingWithSuffix(8, "尺码", SUFFIX)
				+ paddingWithSuffix(6, "数量", SUFFIX)
				+ paddingWithSuffix(8, "单价", SUFFIX)
				+ paddingWithSuffix(8, "金额", SUFFIX);
		device.printString(title);
		List<Item> itemList = record.getItemList();
		for (Item item : itemList) {
			device.printString(paddingWithSuffix(10, item.getName(), SUFFIX)
					+ paddingWithSuffix(8, item.getColor(), SUFFIX)
					+ paddingWithSuffix(8, item.getSize(), SUFFIX)
					+ paddingWithSuffix(8, item.getAmount(), SUFFIX)
					+ paddingWithSuffix(6, item.getUnitPrice(), SUFFIX)
					+ paddingWithSuffix(8, item.getPrice(), SUFFIX));
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

	@Override
	public void modifyOrderPayTypeByOrderId(String orderId, String payType) {
		orderDAO.updateOrderPayTypeById(orderId, payType);
	}

	@Override
	public void modifyOrderStateById(String orderId, String state) {
		orderDAO.updateOrderStateById(orderId, state);
	}

	@Override
	public void modifyOrderRemarkById(String orderId, String remark) {
		orderDAO.updateOrderRemarkById(orderId, remark);
	}

	@Override
	public void printOrder(String orderId, String operatorId, String storeId,
			boolean isRePrint) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		OrderDto dto = getOrderDtoById(orderId);
		dataMap.put("order", (String) access(dto).toString());
		dataMap.put("isRePrint", isRePrint ? "1" : "0");
		Store store = storeService.getStoreById(storeId);
		JsonObject storeObj = new JsonObject();
		storeObj.addProperty("name", store.getName());
		storeObj.addProperty("address", store.getAddress());
		storeObj.addProperty("telePhone", store.getTelePhone());
		storeObj.addProperty("mobilePhone", store.getMobilePhone());
		storeObj.addProperty("reminder", store.getReminder());
		dataMap.put("store", storeObj.toString());
		Client client = clientService.getClientById(dto.getCusId());
		JsonObject clientObj = new JsonObject();
		clientObj.addProperty("name", client.getName());
		dataMap.put("client", clientObj.toString());
		// 目前先配死
		// TODO 以后能直连之后需要从表里读取店铺对应的url地址
		String clientUrl = config.getString("remote.url");
		String privateKey = config.getString("private.key");
		dataMap.put("privateKey", privateKey);
		// 组装成json发送
		logger.info("发送的数据:" + dataMap.toString());
		String back = HttpClientUtil.sendPost(clientUrl + "/remotePrint",
				dataMap, "UTF-8");
		if (!StringUtils.isEmpty(back)) {
			logger.info("请求返回信息:" + back);
		}
	}

	@Override
	public Map<String, Object> printReport(String beginDate, String endDate,
			String storeId) {
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		orderSearch.setBeginDate(beginDate);
		orderSearch.setEndDate(endDate);
		// 将结束时间自动加一天
		Date nextDate = DateUtils
				.addDays(DateUtils.convert2Date(orderSearch.getEndDate(),
						"yyyyMMdd"), 1);
		orderSearch.setEndDate(DateUtils.convert2String(nextDate, "yyyyMMdd"));
		orderSearch.setStoreId(storeId);
		List<OrderDto> orderList = getOrderDtoByCondition(orderSearch);
		if (orderList.size() == 0) {
			return new HashMap<String, Object>();
		}
		// 组装数据
		Map<String, List<OrderDto>> day2recordMap = new LinkedHashMap<String, List<OrderDto>>();
		for (OrderDto orderDto : orderList) {
			List<OrderDto> records = day2recordMap.get(orderDto
					.getCreationTime());
			if (null == records || records.size() == 0) {
				records = new ArrayList<OrderDto>();
				day2recordMap.put(orderDto.getCreationTime(), records);
			}
			records.add(orderDto);
		}
		// 开始数据组装 最终通过post传输
		JsonArray recordsArr = new JsonArray();
		for (Map.Entry<String, List<OrderDto>> entry : day2recordMap.entrySet()) {
			String dateStr = entry.getKey();
			List<OrderDto> records = entry.getValue();
			JsonArray orderDtoArr = new JsonArray();
			if (records.size() == 1) {// 为了防止出现 jsonArray无法转换成JSOnObj的错误
				orderDtoArr.add((JsonObject) access(records
						.toArray(new OrderDto[0])));
			} else {
				orderDtoArr = (JsonArray) access(records
						.toArray(new OrderDto[0]));
			}
			JsonObject record = new JsonObject();
			record.addProperty("dateStr", dateStr);
			record.add("orders", orderDtoArr);
			recordsArr.add(record);
		}
		String privateKey = config.getString("private.key");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("privateKey", privateKey);
		dataMap.put("dataArr", recordsArr);
		return dataMap;

	}

	private Object access(OrderDto... records) {
		JsonObject resultObj = new JsonObject();
		if (null == records) {
			return resultObj.toString();
		}
		JsonArray resultArr = new JsonArray();
		for (OrderDto dto : records) {
			JsonObject dtoJson = new JsonObject();
			dtoJson.addProperty("id", dto.getId());
			dtoJson.addProperty("operatorId", dto.getOperatorId());
			dtoJson.addProperty("storeId", dto.getStoreId());
			dtoJson.addProperty("creationTime", dto.getCreationTime());
			dtoJson.addProperty(
					"cusName",
					StringUtils.isEmpty(dto.getCusName()) ? "" : dto
							.getCusName());
			dtoJson.addProperty("remark",
					StringUtils.isEmpty(dto.getRemark()) ? "" : dto.getRemark());
			dtoJson.addProperty("state", dto.getState());
			List<Item> itemList = dto.getItemList();
			if (null != itemList && itemList.size() > 0) {
				JsonArray itemsArr = new JsonArray();
				for (Item item : itemList) {
					JsonObject itemObj = new JsonObject();
					itemObj.addProperty("orderId", item.getOrderId());
					itemObj.addProperty("id", item.getId());
					itemObj.addProperty("name", item.getName());
					itemObj.addProperty("color", item.getColor());
					itemObj.addProperty("size", item.getSize());
					itemObj.addProperty("unitPrice", item.getUnitPrice());
					itemObj.addProperty("amount", item.getAmount());
					itemObj.addProperty("price", item.getPrice());
					itemsArr.add(itemObj);
				}
				dtoJson.add("itemList", itemsArr);
			}
			dtoJson.addProperty("smallCount", dto.getSmallCount());
			dtoJson.addProperty("totalAmount", dto.getTotalAmount());
			dtoJson.addProperty("payType", dto.getPayType());
			dtoJson.addProperty("address", dto.getAddress());
			dtoJson.addProperty("remark", dto.getRemark());
			if (records.length == 1) {
				return dtoJson;
			} else {
				resultArr.add(dtoJson);
			}
		}
		return resultArr;
	}

	@Override
	public List<Map<String, Object>> searchReport(String beginDate,
			String endDate, String storeId) throws Exception {
		OrderSearchEntity orderSearch = new OrderSearchEntity();
		orderSearch.setBeginDate(beginDate);
		orderSearch.setEndDate(endDate);
		// 将结束时间自动加一天
		Date nextDate = DateUtils
				.addDays(DateUtils.convert2Date(orderSearch.getEndDate(),
						"yyyyMMdd"), 1);
		orderSearch.setEndDate(DateUtils.convert2String(nextDate, "yyyyMMdd"));
		orderSearch.setStoreId(storeId);
		List<OrderDto> orderList = getOrderDtoByCondition(orderSearch);
		if (orderList.size() == 0) {
			return new ArrayList<Map<String, Object>>();
		}
		// 组装数据
		Map<String, List<OrderDto>> day2recordMap = new LinkedHashMap<String, List<OrderDto>>();
		for (OrderDto orderDto : orderList) {
			List<OrderDto> records = day2recordMap.get(orderDto
					.getCreationTime());
			if (null == records || records.size() == 0) {
				records = new ArrayList<OrderDto>();
				day2recordMap.put(orderDto.getCreationTime(), records);
			}
			records.add(orderDto);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map.Entry<String, List<OrderDto>> entry : day2recordMap.entrySet()) {
			Map<String, Object> dataMap = new TreeMap<String, Object>();
			String dateStr = entry.getKey();
			dataMap.put("dateStr", dateStr);
			double cash = 0;
			double card = 0;
			double zfb = 0;
			double weixin = 0;
			double monthPay = 0;
			double dayTotalPrice = 0;
			List<OrderDto> records = entry.getValue();
			// 对map的key进行排序
			Map<String, Integer> nameAndPrice2AmountMap = new TreeMap<String, Integer>();
			for (OrderDto record : records) {
				// 过滤未付款订单
				if (record.getPayType().equals(
						PayTypeEnum.NOTPAY.getType() + "")) {
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
				if (record.getPayType().equals(
						PayTypeEnum.WEIXIN.getType() + "")) {
					weixin += Double.parseDouble(record.getTotalAmount());
				}
				if (record.getPayType().equals(
						PayTypeEnum.MONTHPAY.getType() + "")) {
					monthPay += Double.parseDouble(record.getTotalAmount());
				}
				dayTotalPrice += Double.parseDouble(record.getTotalAmount());
				calculateCount(record, nameAndPrice2AmountMap);
			}
			List<String> sellsStr = new ArrayList<String>();
			for (Map.Entry<String, Integer> _entry : nameAndPrice2AmountMap
					.entrySet()) {
				String[] nameAndPriceArr = _entry.getKey().split(",");
				Integer amount = _entry.getValue();
				String name = nameAndPriceArr[0];
				String price = nameAndPriceArr[1];
				sellsStr.add("当天款式:" + name + " 单价:" + price + " 共卖出:" + amount
						+ "件");
			}
			sellsStr.add("当天现金:" + cash + "元");
			sellsStr.add("当天刷卡:" + card + "元");
			sellsStr.add("当天支付宝:" + zfb + "元");
			sellsStr.add("当天微信:" + weixin + "元");
			sellsStr.add("当天月结:" + monthPay + "元");
			sellsStr.add("当天销售额:" + dayTotalPrice + "元");
			dataMap.put("sellsStr", sellsStr);
			resultList.add(dataMap);
		}

		return resultList;

	}

	private void calculateCount(OrderDto record,
			Map<String, Integer> nameAndPrice2AmountMap) {
		List<Item> itemList = record.getItemList();
		for (Item item : itemList) {
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
	}

	@Override
	public List<String> getSalesVolumeTopByCondition(String storeId) {
		// 获取7天内销量前三的款号
		String endDate = DateUtils.convert2String(
				DateUtils.addDays(new Date(), 1), "yyyyMMdd");
		String startDate = DateUtils.convert2String(
				DateUtils.addDays(new Date(), -6), "yyyyMMdd");
		int top = config.getInt("top");
		return orderDAO.findSalesVolumeTopByCondition(storeId, startDate,
				endDate, top);
	}

}
