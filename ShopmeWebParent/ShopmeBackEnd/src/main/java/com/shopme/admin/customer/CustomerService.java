package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {

	public static final int CUSTOMER_PER_PAGE = 10;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Customer> listAll() {
		return (List<Customer>) customerRepo.findAll();
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
	}

	public void updateCustomerEnabledStatus(Integer id, Boolean enabled) {
		customerRepo.updateEnabledStatus(enabled, id);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any Customer with ID " + id);
		}
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(Integer id, String email) {
		Customer customerInDB = customerRepo.findByEmail(email);

		if (customerInDB != null && customerInDB.getId() != id) {
			return false;
		}

		return true;
	}

	public Customer save(Customer customerInForm) throws CustomerNotFoundException {
		try {
			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
				customerInForm.setPassword(encodedPassword);
			} else {
				customerInForm.setPassword(customerInDB.getPassword());
			}
			
			customerInForm.setCreatedTime(customerInDB.getCreatedTime());
			customerInForm.setEnabled(customerInDB.getEnabled());
			customerInForm.setVerificationCode(customerInDB.getVerificationCode());
			customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
			
			return customerRepo.save(customerInForm);
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not find any Customer with ID " + customerInForm.getId());
		}
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		Long countById = customerRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new CustomerNotFoundException("Could not find any Customer with ID " + id);
		}

		customerRepo.deleteById(id);
	}

}
