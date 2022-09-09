package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

public interface AddressRepository extends CrudRepository<Address, Integer> {

	public List<Address> findByCustomer(Customer customer);

	public Address findByIdAndCustomer(Integer id, Customer customer);

	public void deleteByIdAndCustomer(Integer id, Customer customer);

	@Modifying
	@Query("UPDATE Address a SET a.defaultForShipping=true WHERE a.id=?1")
	public void setDefaultAddress(Integer id);

	@Modifying
	@Query("UPDATE Address a SET a.defaultForShipping=false WHERE"//
			+ " a.id != ?1 AND a.customer.id = ?2")
	public void setNonDefaultForOthers(Integer defaultAddressId, Integer CustomerId);

}
