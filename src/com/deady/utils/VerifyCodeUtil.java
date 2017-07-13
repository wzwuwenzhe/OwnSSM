package com.deady.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

/**
 * @author Andre.Z 2014-11-3 下午5:16:15<br>
 * 
 *         图形校验码处理
 * 
 */
public class VerifyCodeUtil {

	private VerifyCodeUtil() {
	}

	// 引入静态内部类实现 懒加载的单例对象
	private static class InnerClass {
		private static final VerifyCodeUtil instance = new VerifyCodeUtil();
	}

	public static VerifyCodeUtil getInstance() {
		return InnerClass.instance;
	}

	public void show(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		this.show(req, res, 60, 20);
	}

	public void show(HttpServletRequest req, HttpServletResponse res,
			int width, int height) throws Exception {
		HttpSession session = req.getSession();
		res.setContentType("image/jpeg");
		ServletOutputStream sos = res.getOutputStream();
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		char[] rands = this.generateCheckCode();
		this.drawBackground(g, width, height);
		this.drawRands(g, rands);
		g.dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		res.setContentLength(buf.length);
		sos.write(buf);
		bos.close();
		sos.close();
		session.setAttribute("_vcode", new String(rands));
	}

	private char[] generateCheckCode() {
		String chars = "0123456789";
		char[] rands = new char[4];
		for (int i = 0; i < 4; i++) {
			int rand = (int) (Math.random() * 10);
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}

	private void drawRands(Graphics g, char[] rands) {
		g.setColor(Color.BLACK);
		g.setFont(new Font(null, Font.CENTER_BASELINE, 18));
		g.drawString("" + rands[0], 1, 17);
		g.drawString("" + rands[1], 16, 15);
		g.drawString("" + rands[2], 31, 18);
		g.drawString("" + rands[3], 46, 16);
	}

	private void drawBackground(Graphics g, int width, int height) {
		g.setColor(new Color(231, 252, 253));
		g.fillRect(0, 0, width, height);
		for (int i = 0; i < 60; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}
	}

	public boolean isValid(HttpServletRequest req) {
		// 将用户传入的验证码 和 session中存入的验证码进行对比校验
		String code = req.getParameter("vcode");
		HttpSession session = req.getSession();
		if (StringUtils.isEmpty(code)
				|| StringUtils.isEmpty(session.getAttribute("_vcode"))) {
			return false;
		}
		boolean valid = String.valueOf(session.getAttribute("_vcode"))
				.equalsIgnoreCase(code);
		if (valid) {
			session.removeAttribute("_vcode");
		}
		return valid;
	}
}
