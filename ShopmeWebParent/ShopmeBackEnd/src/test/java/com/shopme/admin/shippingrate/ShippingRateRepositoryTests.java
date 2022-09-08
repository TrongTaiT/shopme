package com.shopme.admin.shippingrate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTests {

	@Autowired
	private ShippingRateRepository repo;

	@Test
	public void testCreateNew1() {
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(15);
		shippingRate.setDays(3);
		shippingRate.setCodSupported(true);
		shippingRate.setCountry(new Country(242));
		shippingRate.setState("Lam Dong");

		ShippingRate savedShippingRate = repo.save(shippingRate);

		assertThat(savedShippingRate.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNew2() {
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(20);
		shippingRate.setDays(1);
		shippingRate.setCodSupported(true);
		shippingRate.setCountry(new Country(242));
		shippingRate.setState("Ho Chi Minh");

		ShippingRate savedShippingRate = repo.save(shippingRate);

		assertThat(savedShippingRate.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNew3() {
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(30);
		shippingRate.setDays(5);
		shippingRate.setCodSupported(false);
		shippingRate.setCountry(new Country(242));
		shippingRate.setState("Bac Kan");

		ShippingRate savedShippingRate = repo.save(shippingRate);

		assertThat(savedShippingRate.getId()).isGreaterThan(0);
	}

	@Test
	public void testUpdate() {
		Integer countryId = 234;
		String state = "Idaho";

		ShippingRate shippingRate = new ShippingRate(1);
		shippingRate.setRate(30);
		shippingRate.setDays(7);
		shippingRate.setCodSupported(false);
		shippingRate.setCountry(new Country(countryId));
		shippingRate.setState(state);

		ShippingRate updatedShippingRate = repo.save(shippingRate);

		assertThat(updatedShippingRate.getRate()).isEqualTo(30);
		assertThat(updatedShippingRate.getDays()).isEqualTo(7);
		assertThat(updatedShippingRate.getCodSupported()).isFalse();
		assertThat(updatedShippingRate.getCountry().getId()).isEqualTo(countryId);
		assertThat(updatedShippingRate.getState()).isEqualTo(state);
	}

	@Test
	public void testDelete() {
		Integer id = 3;

		repo.deleteById(id);

		Optional<ShippingRate> result = repo.findById(id);

		assertThat(result.isEmpty());
	}

	@Test
	public void testFindAllWithoutKeyword() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ShippingRate> page = repo.findAll("", pageable);
		List<ShippingRate> list = page.getContent();
		list.stream().forEach(System.out::println);

		assertThat(list).hasSize(3);
	}

	@Test
	public void testFindAllWithKeyword() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ShippingRate> page = repo.findAll("Lam Dong", pageable);
		List<ShippingRate> list = page.getContent();
		list.stream().forEach(System.out::println);

		assertThat(list).hasSize(1);
	}

	@Test
	public void testFindByCountryAndState() {
		Country country = new Country(242);
		String state = "Lam Dong";

		ShippingRate shippingRate = repo.findByCountryAndState(country, state);

		System.out.println(shippingRate);

		assertThat(shippingRate).isNotNull();
	}

	@Test
	public void testUpdateCODSupport() {
		Integer id = 3;
		boolean enabled = true;

		repo.updateCODSupport(id, enabled);
		
		ShippingRate shippingRate = repo.findById(id).get();
		assertThat(shippingRate.getCodSupported()).isEqualTo(enabled);
	}

}
