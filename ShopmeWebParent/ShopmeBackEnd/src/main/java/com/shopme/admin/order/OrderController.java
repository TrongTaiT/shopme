package com.shopme.admin.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.exception.OrderNotFoundException;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;

	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

	@GetMapping("/orders")
	public String listFirstPage() {
		return defaultRedirectURL;
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage( //
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper, //
			@PathVariable(name = "pageNum") int pageNum, //
			HttpServletRequest request) //
	{
		orderService.listByPage(pageNum, helper);
		loadCurrencySetting(request);

		return "orders/orders";
	}

	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();

		currencySettings.forEach(setting -> //
		request.setAttribute(setting.getKey(), setting.getValue()));
	}

	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails( //
			Model model, //
			@PathVariable("id") Integer orderId, //
			RedirectAttributes ra, //
			HttpServletRequest request) //
	{
		try {
			Order order = orderService.get(orderId);

			loadCurrencySetting(request);
			model.addAttribute("order", order);
			System.out.println("ORder: " + order.getDeliverDate());

			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/orders/delete/{id}")
	public String deleteOrder( //
			@PathVariable("id") Integer orderId, //
			RedirectAttributes ra) //
	{
		try {
			orderService.delete(orderId);
			
			ra.addFlashAttribute("message", "The order with ID " + orderId + " has been deleted successfully.");
		} catch (OrderNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());			
		}
		
		return defaultRedirectURL;
	}

}
