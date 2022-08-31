package com.shopme.customer;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if (customer == null || customer.getEnabled()) {
			return false;
		}

		customerRepo.enable(customer.getId());
		return true;
	}

	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	public void addNewCustomerUponOAuthLogin(String name, String email, //
			String countryCode, AuthenticationType authenticationType) {
		Customer customer = new Customer();
		setName(name, customer);

		customer.setEmail(email);
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));

		customerRepo.save(customer);
	}

	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if (nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);

			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}

	public Customer update(Customer customerInForm) throws CustomerNotFoundException {
		try {
			Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

			if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
				if (!customerInForm.getPassword().isEmpty()) {
					String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
					customerInForm.setPassword(encodedPassword);
				} else {
					customerInForm.setPassword(customerInDB.getPassword());
				}
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

}
