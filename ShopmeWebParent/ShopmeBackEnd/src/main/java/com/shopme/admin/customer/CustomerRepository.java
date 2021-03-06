package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE CONCAT(c.firstName, ' ', " //
			+ "c.lastName, ' ', c.email, ' ', c.addressLine1, ' ', " //
			+ "c.addressLine2, ' ', c.city, ' ', c.state, ' ', " //
			+ "c.country.name, ' ', c.postalCode) LIKE %?1%") //
	public Page<Customer> findAll(String keyword, Pageable pageable);

	public Customer findByEmail(String email);

	@Query("UPDATE Customer c SET c.enabled = ?1 WHERE c.id = ?2")
	@Modifying
	public void updateEnabledStatus(Boolean enabled, Integer id);

	public Long countById(Integer id);

}
