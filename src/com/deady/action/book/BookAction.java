package com.deady.action.book;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.deady.annotation.DeadyAction;
import com.deady.entity.book.Book;
import com.deady.service.BookService;

@Controller
public class BookAction {

	// 注入service
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/booklist", method = RequestMethod.GET)
	@DeadyAction(checkReferer = true, checkLogin = false)
	public Object bookList(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		List<Book> bookList = bookService.getList();
		req.setAttribute("booklist", bookList);
		return new ModelAndView("/book/list");
	}
}
