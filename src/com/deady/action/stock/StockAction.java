package com.deady.action.stock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	@DeadyAction(checkLogin = true)
	public Object putInStock(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		List<Factory> factoryList = factoryService.getFactoryListByStoreId(op
				.getStoreId());
		if (null != factoryList && factoryList.size() > 0) {
			req.setAttribute("factoryList", factoryList);
		}
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

	@RequestMapping(value = "/putInStock", method = RequestMethod.POST)
	@DeadyAction(checkLogin = true)
	@ResponseBody
	public Object doPutInStock(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		Stock stock = new Stock();
		ActionUtil.assObjByRequest(req, stock);
		String year = ActionUtil.getLunarCalendarYear();
		Operator op = OperatorSessionInfo.getOperator(req);
		stock.setYear(year);
		stock.setStoreId(op.getStoreId());
		stockService.addStock(stock);
		// 查一下已存在的产品库存 是否已经拥有该款号
		List<Storage> storageList = stockService.getStorageByStoreId(op
				.getStoreId());
		boolean exists = false;
		for (Storage s : storageList) {
			if (s.getYear().equals(year) && s.getName().equals(stock.getName())) {
				exists = true;
				// 合计
				String total = s.getTotal();
				// 剩余库存
				String stockLeft = s.getStockLeft();
				s.setTotal((Integer.parseInt(total) + Integer.parseInt(stock
						.getAmount())) + "");
				s.setStockLeft((Integer.parseInt(stockLeft) + Integer
						.parseInt(stock.getAmount())) + "");
				stockService.updateStorage(s);
				break;
			}
		}
		if (!exists) {
			Storage storage = new Storage(op.getStoreId(), year,
					stock.getName(), stock.getAmount(), stock.getAmount());
			stockService.addStorage(storage);
		}
		response.setMessage("入库成功!");
		response.setSuccess(true);
		return response;
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
		//storage  和 stock 两个表一起删了
		stockService.removeStorage(name, year, op.getStoreId());
		response.setSuccess(true);
		response.setMessage("库存删除成功!");
		return response;
	}
}
