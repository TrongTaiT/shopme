package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepo.findAll(keyword, pageable);
		}

		return customerRepo.findAll(pageable);
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
			
			customerInForm.setEnabled(customerInDB.getEnabled());
			
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
