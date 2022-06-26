package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/customers")
	public String listFirstPage() {
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage( //
			@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper, //
			@PathVariable("pageNum") int pageNum) //
	{
		customerService.listByPage(pageNum, helper);

		return "customers/customers";
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus( //
			@PathVariable("id") Integer id, //
			@PathVariable("status") Boolean enabled, //
			RedirectAttributes ra) //
	{
		customerService.updateCustomerEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The customer with ID " + id + " has been " + status;

		ra.addFlashAttribute("message", message);

		return "redirect:/customers";
	}

	@GetMapping("/customers/detail/{id}")
	public String viewCustomer( //
			Model model, //
			@PathVariable("id") Integer id, //
			RedirectAttributes ra) //
	{
		try {
			Customer customer = customerService.get(id);

			model.addAttribute("customer", customer);

			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}

	@GetMapping("/customers/edit/{id}")
	public String editCustomer( //
			Model model, //
			RedirectAttributes ra, //
			@PathVariable("id") Integer id) //
	{
		try {
			Customer customer = customerService.get(id);
			List<Country> listCountries = customerService.listAllCountries();

			model.addAttribute("customer", customer);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");

			return "customers/customer_form";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
	}

	@PostMapping("/customers/save")
	public String saveCustomer( //
			RedirectAttributes ra, //
			Customer customer) //
	{
		try {
			customerService.save(customer);
			ra.addFlashAttribute("message", "The customer has been saved successfully.");
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/customers";
	}

	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer( //
			RedirectAttributes ra, //
			@PathVariable("id") Integer id) //
	{
		try {
			customerService.delete(id);
			ra.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully");
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/customers";
	}

}
