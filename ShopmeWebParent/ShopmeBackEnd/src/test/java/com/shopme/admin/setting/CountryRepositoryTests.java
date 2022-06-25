package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {

	@Autowired
	private CountryRepository repo;

	@Test
	public void testCreateCountry1() {
		Country country = new Country("Vietnam", "vn");

		Country savedCountry = repo.save(country);

		assertThat(savedCountry.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateCountry2() {
		Country country = new Country("United State", "us");

		Country savedCountry = repo.save(country);

		assertThat(savedCountry.getId()).isGreaterThan(0);
	}

	@Test
	public void testListCountries() {
		Iterable<Country> result = repo.findAllByOrderByNameAsc();

		result.forEach(System.out::println);

		assertThat(result).size().isGreaterThan(0);
	}

	@Test
	public void testGetCountry() {
		Integer id = 1;
		Country country = repo.findById(id).get();

		assertThat(country).isNotNull();
	}

	@Test
	public void testUpdateCountry() {
		Integer id = 1;
		String newName = "Việt Nam";
		Country country = repo.findById(id).get();

		country.setName(newName);

		Country updatedCountry = repo.save(country);

		assertThat(updatedCountry.getName()).isEqualTo(newName);
	}

	@Test
	public void testDeleteCountry() {
		Integer id = 2;
		repo.deleteById(id);

		Optional<Country> result = repo.findById(id);

		assertThat(result).isEmpty();
	}

}
