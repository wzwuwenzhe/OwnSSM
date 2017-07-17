package com.deady.action.register;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.operator.Operator;
import com.deady.entity.store.Store;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class StoreRegisterAction {

	private static final String SPRIT = "/";

	@RequestMapping(value = "/storeRegister", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object storeRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		req.setAttribute("storeId", op.getStoreId());
		return new ModelAndView("/register/store_register");
	}

	@RequestMapping(value = "/storeRegister", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doStoreRegister(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		Store store = new Store();
		ActionUtil.assObjByRequest(req, store);
		FormResponse response = new FormResponse(req);
		// TODO 数据插到库里
		response.setSuccess(true);
		return response;
	}

	@RequestMapping(value = "/storeRegister4Img", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object doStoreRegister4Img(@RequestParam("file") MultipartFile file,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		Operator op = OperatorSessionInfo.getOperator(req);
		String filePath = ActionUtil.getImgUploadPath() + "ImgUpload/"
				+ op.getStoreId() + SPRIT;
		String fileName = ActionUtil.getImgUploadPath() + "ImgUpload/"
				+ op.getStoreId() + SPRIT + file.getOriginalFilename();
		if (saveFile(file, filePath, fileName)) {
			response.setSuccess(true);
			response.setMessage(fileName);
		} else {
			response.setSuccess(false);
			response.setMessage("上传失败!");
		}
		return response;
	}

	/***
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 */
	private boolean saveFile(MultipartFile file, String filePath,
			String fileName) {
		if (!file.isEmpty()) {
			File uFile = new File(filePath);
			if (!uFile.exists()) {
				uFile.mkdirs();
			}
			try {
				file.transferTo(new File(fileName));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
