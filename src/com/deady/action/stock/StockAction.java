package com.deady.action.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.deady.entity.factory.Factory;
import com.deady.entity.operator.Operator;
import com.deady.entity.stock.Stock;
import com.deady.entity.stock.Storage;
import com.deady.service.FactoryService;
import com.deady.service.StockService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * 库存管理
 * 
 * @author wzwuw
 * 
 */
@Controller
public class StockAction {

	@Autowired
	private StockService stockService;
	@Autowired
	private FactoryService factoryService;

	/**
	 * 显示库存清单
	 */
	@RequestMapping(value = "/showStockManage", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object showStockManage(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		return new ModelAndView("/stock/stockManage");
	}

	@RequestMapping(value = "/putInStock", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true, createToken = true)
	public Object putInStock(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Factory> factoryList = factoryService.getFactoryListByStoreId(op
				.getStoreId());
		req.setAttribute("factoryList", factoryList);
		if (null != req.getParameter("name")) {
			// 款号
			String name = req.getParameter("name");
			req.setAttribute("name", name);
		}
		return new ModelAndView("/stock/putInStock");
	}

	@RequestMapping(value = "/storageList", method = RequestMethod.GET)
	@DeadyAction(checkLogin = true)
	public Object showStockList(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Storage> storageList = stockService.getStorageDtoByStoreId(op
				.getStoreId());
		req.setAttribute("storageList", storageList);
		return new ModelAndView("/stock/storageList");
	}

	// 根据款号 找到款号对应的所有颜色以及尺码
	@RequestMapping(value = "/getCorlorAndSizeByName", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object getColorAndSizeByName(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String name = req.getParameter("name");
		if (StringUtils.isEmpty(name)) {
			resultMap.put("result", "0");
			resultMap.put("message", "参数错误!");
			return resultMap;
		}
		Operator op = OperatorSessionInfo.getOperator(req);
		Storage storage = stockService.getStorageByNameAndStoreId(name,
				op.getStoreId());
		if (null == storage) {
			resultMap.put("result", "1");
			resultMap.put("message", "找不到相应的款式");
		} else {
			String colors = storage.getColors();
			String sizes = storage.getSizes();
			resultMap.put("result", "2");
			resultMap.put("colors", colors);
			resultMap.put("sizes", sizes);
		}
		return resultMap;
	}

	/**
	 * 入库操作
	 * 
	 * @param color
	 *            单条记录的颜色
	 * @param size
	 *            单条记录的尺寸
	 * @param amount
	 *            单条记录的数量
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/putInStock", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doPutInStock(HttpServletRequest req, HttpServletResponse res,
			String[] color, String[] size, String[] amount) throws Exception {
		FormResponse response = new FormResponse(req);
		if (null == color || null == size || null == amount) {
			response.setSuccess(false);
			response.setMessage("请先添加入库信息!");
			return response;
		}
		Set<String> colorSet = new HashSet<String>();// 保存所有的颜色信息
		Set<String> sizeSet = new HashSet<String>();// 保存所有的尺寸信息
		// 相同款号和颜色 只能保存一份数据 (合并重复数据)
		Map<String, Stock> colorAndSize2StockMap = new HashMap<String, Stock>();
		List<Stock> stockList = new ArrayList<Stock>();// 需要新增的Stock列表
		Operator op = OperatorSessionInfo.getOperator(req);
		String name = req.getParameter("name");// 款号
		String year = ActionUtil.getLunarCalendarYear();
		String factoryId = req.getParameter("factoryId");
		int onceTotal = 0;
		for (int i = 0; i < color.length; i++) {
			colorSet.add(color[i]);
			sizeSet.add(size[i]);
			String colorAndSizeKey = color[i] + "," + size[i];
			Stock stockValue = colorAndSize2StockMap.get(colorAndSizeKey);
			if (null == stockValue) {
				Stock stock = new Stock(color[i], size[i], amount[i],
						op.getStoreId(), year, name, factoryId);
				stockList.add(stock);
				colorAndSize2StockMap.put(colorAndSizeKey, stock);
			} else {
				int lastAmount = Integer.parseInt(stockValue.getAmount());
				int nowAmount = Integer.parseInt(amount[i]);
				stockValue.setAmount((lastAmount + nowAmount) + "");
				stockList.add(stockValue);
				colorAndSize2StockMap.put(colorAndSizeKey, stockValue);
			}
			// 累计单次入库总数
			onceTotal += Integer.parseInt(amount[i]);

		}
		// stock入库 //TODO 应该需要设计一个事务 把添加stock和修改storage 放在同一个事务下
		stockService.addStocks(stockList);
		// 查一下已存在的产品库存 是否已经拥有该款号
		List<Storage> storageList = stockService.getStorageByStoreId(op
				.getStoreId());
		boolean exists = false;
		for (Storage s : storageList) {
			if (s.getYear().equals(year) && s.getName().equals(name)) {
				exists = true;
				// 合计
				String total = s.getTotal();
				// 剩余库存
				String stockLeft = s.getStockLeft();
				s.setTotal((Integer.parseInt(total) + onceTotal) + "");
				s.setStockLeft((Integer.parseInt(stockLeft) + onceTotal) + "");
				// 合并款号对应的所有颜色和尺寸
				String[] colors = s.getColors().split(",");
				String[] sizes = s.getSizes().split(",");
				for (int i = 0; i < colors.length; i++) {
					colorSet.add(colors[i]);
				}
				for (int i = 0; i < sizes.length; i++) {
					sizeSet.add(size[i]);
				}
				// 去空格 去两头括号
				s.setColors(dealwithSet(colorSet));
				s.setSizes(dealwithSet(sizeSet));
				stockService.updateStorage(s);
				break;
			}
		}
		if (!exists) {
			Storage storage = new Storage(op.getStoreId(), year, name,
					onceTotal + "", onceTotal + "", dealwithSet(colorSet),
					dealwithSet(sizeSet));
			stockService.addStorage(storage);
		}
		response.setMessage("入库成功!");
		response.setSuccess(true);
		return response;
	}

	private String dealwithSet(Set<String> set) {
		return set.toString().substring(1, set.toString().length() - 1)
				.replaceAll(" ", "");
	}

	public static void main(String[] args) {
		Set<String> colorSet = new HashSet<String>();// 保存所有的颜色信息
		colorSet.add("红色");
		colorSet.add("及黄色");
		colorSet.add("大师傅色");
		System.out.println(colorSet.toString()
				.substring(1, colorSet.toString().length() - 1)
				.replaceAll(" ", ""));

	}

	/**
	 * 删除库存
	 */

	@RequestMapping(value = "/deleteStorage", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object doDeleteStorage(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		String name = req.getParameter("name");
		String year = ActionUtil.getLunarCalendarYear();
		Operator op = OperatorSessionInfo.getOperator(req);
		// storage 和 stock 两个表一起删了
		stockService.removeStorage(name, year, op.getStoreId());
		response.setSuccess(true);
		response.setMessage("库存删除成功!");
		return response;
	}
}
