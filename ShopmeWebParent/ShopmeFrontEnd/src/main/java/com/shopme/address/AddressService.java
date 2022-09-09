package com.shopme.address;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.AddressNotFoundException;

@Service
@Transactional
public class AddressService {

	@Autowired
	private AddressRepository repo;

	public List<Address> listAddressBook(Customer customer) {
		return repo.findByCustomer(customer);
	}

	public void save(Address address) {
		repo.save(address);
	}

	public Address get(Integer id, Customer customer) throws AddressNotFoundException {
		try {
			return repo.findByIdAndCustomer(id, customer);
		} catch (NoSuchElementException e) {
			throw new AddressNotFoundException("Could not find the requested address.");
		}
	}

	public void delete(Integer id, Customer customer) throws AddressNotFoundException {
		try {
			repo.deleteByIdAndCustomer(id, customer);
		} catch (NoSuchElementException e) {
			throw new AddressNotFoundException("Could not find the requested address.");
		}
	}

}
