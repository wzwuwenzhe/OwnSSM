package com.deady.action.register;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cnblogs.zxub.utils2.configuration.ConfigUtil;
import com.deady.annotation.DeadyAction;
import com.deady.common.FormResponse;
import com.deady.entity.operator.Operator;
import com.deady.entity.store.Store;
import com.deady.service.StoreService;
import com.deady.utils.ActionUtil;
import com.deady.utils.OperatorSessionInfo;

@Controller
public class StoreRegisterAction {

	@Autowired
	private StoreService storeService;

	private static final String SPRIT = "/";
	private static final String ADMIN = "1";

	@RequestMapping(value = "/storeRegister", method = RequestMethod.GET)
	@DeadyAction(createToken = true)
	public Object storeRegister(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		Operator op = OperatorSessionInfo.getOperator(req);
		req.setAttribute("storeId", op.getStoreId());
		Store store = null;
		if (op.getUserType().equals(ADMIN)) {
			String storeId = req.getParameter("storeId");
			req.setAttribute("storeId",
					StringUtils.isEmpty(storeId) ? op.getStoreId() : storeId);
			store = storeService.getStoreById(StringUtils.isEmpty(storeId) ? op
					.getStoreId() : storeId);
		} else {
			store = storeService.getStoreById(op.getStoreId());
		}
		if (null != store) {
			req.setAttribute("store", store);
		}
		return new ModelAndView("/register/store_register");
	}

	@RequestMapping(value = "/storeRegister", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doStoreRegister(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		try {
			Store store = validateStore(req, res, response);
			storeService.addStore(store);
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		response.setSuccess(true);
		response.setMessage("店铺注册成功!");
		response.setData("/index");
		return response;
	}

	@RequestMapping(value = "/storeModify", method = RequestMethod.POST)
	@DeadyAction(createToken = true, checkToken = true)
	@ResponseBody
	public Object doStoreModify(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		FormResponse response = new FormResponse(req);
		Operator op = OperatorSessionInfo.getOperator(req);
		try {
			Store store = validateStore(req, res, response);
			if (op.getUserType().equals("1")) {// 超管可以改秘钥和回调地址
				storeService.modifyStore4Admin(store);
			} else {
				storeService.modifyStore4Owner(store);
			}
		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage(e.getMessage());
			return response;
		}
		response.setSuccess(true);
		response.setMessage("店铺修改成功!");
		response.setData("/storeModify");
		return response;
	}

	private Store validateStore(HttpServletRequest req,
			HttpServletResponse res, FormResponse response) throws Exception {
		Store store = new Store();
		ActionUtil.assObjByRequest(req, store);
		// 处理store中的图片路径
		handleStore(store);
		Operator op = OperatorSessionInfo.getOperator(req);
		if (!op.getUserType().equals(ADMIN)) {
			if (!op.getStoreId().trim().equals(store.getId().trim())) {
				throw new Exception("店铺ID被修改,无法创建!");
			}
		}
		return store;
	}

	private void handleStore(Store store) {
		store.setLogoImg(handlePath(store.getLogoImg()));
		store.setWxAddImg(handlePath(store.getWxAddImg()));
		store.setWxPayImg(handlePath(store.getWxPayImg()));
		store.setZfbPayImg(handlePath(store.getZfbPayImg()));
	}

	private String handlePath(String img) {
		if (StringUtils.isEmpty(img)) {
			return "";
		}
		int start = img.indexOf("/ImgUpload/");
		return img.substring(start, img.length());
	}

	@RequestMapping(value = "/storeRegister4Img", method = RequestMethod.POST)
	@DeadyAction(createToken = true)
	@ResponseBody
	public Object doStoreRegister4Img(@RequestParam("file") MultipartFile file,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		FormResponse response = new FormResponse(req);
		Operator op = OperatorSessionInfo.getOperator(req);
		String fileNewName = req.getParameter("fileName");
		// 文件格式检测
		if (!fileNameMatch(file.getOriginalFilename())) {
			response.setSuccess(false);
			response.setMessage("文件格式不匹配,(仅支持jpg、png、bmp格式图片)");
			return response;
		}
		String storeId = op.getStoreId();
		// 超管可以改其他店铺的信息
		if (op.getUserType().equals(ADMIN)) {
			storeId = req.getParameter("storeId");
		}
		String filePath = ActionUtil.getImgUploadPath() + "ImgUpload/"
				+ storeId + SPRIT;
		if (saveFile(file, filePath, file.getOriginalFilename(), fileNewName)) {
			response.setSuccess(true);
			// 这里用将tomcat是server.xml进行配置
			// 使http://localhost:9080/ImgUpload/05719999/logoImgFile.png 能转发到
			// 本地路径c:/temp/ImgUpload/05719999/logoImgFile.png
			String PREFIX = req.getScheme() + "://" + req.getServerName() + ":"
					+ req.getServerPort() + SPRIT + "ImgUpload" + SPRIT;
			response.setMessage(PREFIX + storeId + SPRIT + fileNewName + ".png");
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(currentTime);
			response.setData(dateString);
		} else {
			response.setSuccess(false);
			response.setMessage("上传失败!");
		}
		return response;
	}

	private boolean fileNameMatch(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return false;
		}

		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		if (fileType.toLowerCase().equals("jpg")
				|| fileType.toLowerCase().equals("png")
				|| fileType.toLowerCase().equals("gif")
				|| fileType.toLowerCase().equals("jpeg")
				|| fileType.toLowerCase().equals("bmp")) {
			return true;
		}
		return false;
	}

	/***
	 * 保存文件
	 * 
	 * @param file
	 * @return
	 */
	private boolean saveFile(MultipartFile file, String filePath,
			String oldName, String newName) {
		if (!file.isEmpty()) {
			File uFile = new File(filePath);
			if (!uFile.exists()) {
				uFile.mkdirs();
			}
			try {
				File fromPic = new File(filePath + oldName + "_temp");
				file.transferTo(fromPic);
				// 原始文件上传成功
				// 对上传的图片进行制定大小处理
				File toPic = new File(filePath + newName + ".png");
				// 此处把图片压成300宽度的缩略图(遵循原图高宽比例)
				Thumbnails.of(fromPic).size(300, 300).toFile(toPic);
				// 删掉原用户上传的图片
				fromPic.delete();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
