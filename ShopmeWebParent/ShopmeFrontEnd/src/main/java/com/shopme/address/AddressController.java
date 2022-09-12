package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.AddressNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);

		boolean usePrimaryAddressAsDefault = listAddresses.stream()//
				.noneMatch(a -> a.getDefaultForShipping() == true);

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}

	@GetMapping("/address_book/new")
	public String newAddress(Model model) {
		List<Country> listCountries = customerService.listAllCountries();

		model.addAttribute("listCountries", listCountries);
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");

		return "address_book/address_form";
	}

	@PostMapping("/address_book/save")
	public String saveAddress( //
			Address address, //
			RedirectAttributes ra, //
			HttpServletRequest request) //
	{
		Customer customer = getAuthenticatedCustomer(request);

		address.setCustomer(customer);
		addressService.save(address);

		ra.addFlashAttribute("message", "The address has been saved successfully.");

		return "redirect:/address_book";
	}

	@GetMapping("/address_book/edit/{id}")
	public String editAddress(//
			@PathVariable("id") Integer id, //
			Model model, //
			RedirectAttributes ra, //
			HttpServletRequest request) //
	{
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Address address = addressService.get(id, customer);
			List<Country> listCountries = customerService.listAllCountries();

			model.addAttribute("listCountries", listCountries);
			model.addAttribute("address", address);
			model.addAttribute("pageTitle", "Edit Address (ID: " + id + ")");

			return "address_book/address_form";
		} catch (AddressNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/address_book";
		}
	}

	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress( //
			@PathVariable("id") Integer id, //
			RedirectAttributes ra, //
			HttpServletRequest request) //
	{
		try {
			Customer customer = getAuthenticatedCustomer(request);
			addressService.delete(id, customer);

			ra.addFlashAttribute("message", "The address with ID " + id + " has been deleted successfully.");
		} catch (AddressNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/address_book";
	}

	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress( //
			@PathVariable("id") Integer addressId, //
			HttpServletRequest request) //
	{
		Customer customer = getAuthenticatedCustomer(request);

		addressService.setDefaultAddress(addressId, customer.getId());
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		}

		return redirectURL;
	}

}
