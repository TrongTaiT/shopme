package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

	@Autowired
	private BrandRepository repo;

	@Test
	public void testCreateBrand1() {
		Category category = new Category(6);
		Brand brand = new Brand("Acer");
		brand.getCategories().add(category);
		brand.setLogo("brand-logo.png");

		Brand savedBrand = repo.save(brand);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand2() {
		Category category1 = new Category(4);
		Category category2 = new Category(7);
		Brand brand = new Brand("Apple");
		brand.getCategories().add(category1);
		brand.getCategories().add(category2);
		brand.setLogo("brand-logo.png");

		Brand savedBrand = repo.save(brand);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateBrand3() {
		Category category1 = new Category(24);
		Category category2 = new Category(29);
		Brand brand = new Brand("Samsung");
		brand.getCategories().add(category1);
		brand.getCategories().add(category2);
		brand.setLogo("brand-logo.png");

		Brand savedBrand = repo.save(brand);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllBrands() {
		Iterable<Brand> result = repo.findAll();

		result.forEach(System.out::println);

		assertThat(result).isNotEmpty();
	}

	@Test
	public void testGetBrandById() {
		Integer id = 3;
		Brand brand = repo.findById(id).get();
		System.out.println(brand);

		assertThat(brand).isNotNull();
	}

	@Test
	public void testUpdateBrand() {
		String newName = "Samsung Electronics";
		
		Brand samsung = repo.findById(4).get();		
		samsung.setName(newName);
		
		Brand updatedBrand = repo.save(samsung);
		
		assertThat(updatedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDeleteBrand() {
		Integer id = 2;
		repo.deleteById(id);
		
		Optional<Brand> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}

}
