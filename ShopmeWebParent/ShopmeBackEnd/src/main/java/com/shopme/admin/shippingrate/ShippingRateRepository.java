package com.shopme.admin.shippingrate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer> {

	@Query("SELECT s FROM ShippingRate s WHERE s.country.name LIKE %?1% "//
			+ "OR s.state LIKE %?1%") //
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);

	public ShippingRate findByCountryAndState(Country country, String state);

	@Modifying
	@Query("UPDATE ShippingRate s SET s.codSupported=?2 WHERE s.id=?1")
	public void updateCODSupport(Integer id, boolean enabled);

	public Long countById(Integer id);

}
