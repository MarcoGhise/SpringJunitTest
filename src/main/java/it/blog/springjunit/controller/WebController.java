package it.blog.springjunit.controller;

import javax.servlet.http.HttpSession;

import it.blog.springjunit.bean.Customer;
import it.blog.springjunit.dao.SqlDao;
import it.blog.springjunit.dao.TableData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

	@Autowired
	SqlDao jdbcDao;

	@RequestMapping(method = RequestMethod.GET, value = "/lastname")
	public String getLastName(HttpSession sessionObj, @RequestParam String firstname, ModelMap model) throws InterruptedException {

		System.out.println(firstname);
		
		model.addAttribute("lastname", firstname);
		
		/*
		if (sessionObj.getAttribute("LastName") == null) {
			TableData tableData = jdbcDao.getData(firstname);

			if (tableData != null)
				model.addAttribute("lastname", tableData.getLast_name());
			else
				model.addAttribute("lastname", "");

			sessionObj.setAttribute("LastName", tableData.getLast_name());
		}
		else
			model.addAttribute("lastname", sessionObj.getAttribute("LastName"));
*/
		return "index";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/lastnamejson", produces = "application/json")
	@ResponseBody
	public Customer getLastNameJson(@RequestParam String firstname, ModelMap model) throws InterruptedException {

		TableData tableData = jdbcDao.getData(firstname);

		Customer customer = new Customer();
		customer.setId(tableData.getId());
		customer.setName(tableData.getLast_name());

		return customer;

	}
}
