package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct1() {
		Brand brand = entityManager.find(Brand.class, 10);
		Category category = entityManager.find(Category.class, 15);

		Product product = new Product();
		product.setName("Samsung Galaxy A31");
		product.setAlias("samsung_galaxy_a31");
		product.setShortDescription("A good smartphone from Samsung");
		product.setFullDescription("This is a very good smartphone - full description");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(456);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateProduct2() {
		Brand brand = entityManager.find(Brand.class, 38);
		Category category = entityManager.find(Category.class, 6);

		Product product = new Product();
		product.setName("Dell Inspirion 3000");
		product.setAlias("dell_inspirion_3000");
		product.setShortDescription("A good laptop from Dell - short description");
		product.setFullDescription("This is a very good laptop - full description");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(456);
		product.setCost(400);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateProduct3() {
		Brand brand = entityManager.find(Brand.class, 37);
		Category category = entityManager.find(Category.class, 5);

		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("A good desktop from Acer - short description");
		product.setFullDescription("This is a very good desktop - full description");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(678);
		product.setCost(600);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();

		iterableProducts.forEach(System.out::println);

		assertThat(iterableProducts).isNotEmpty();
	}

	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = repo.findById(id).get();

		System.out.println(product);

		assertThat(product).isNotNull();
	}

	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = repo.findById(id).get();
		product.setPrice(500);

		repo.save(product);

		Product updatedProduct = entityManager.find(Product.class, id);

		assertThat(updatedProduct.getPrice()).isEqualTo(product.getPrice());
	}

	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repo.deleteById(id);

		Optional<Product> result = repo.findById(id);
		
		assertThat(result.isEmpty());
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = repo.findById(productId).get();
		
		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.jpg");
		product.addExtraImage("extra_image_2.jpg");
		product.addExtraImage("extra-image-3.jpg");
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
	
	@Test
	public void testSaveProductWithDetails() {
		Integer productId = 2;
		Product product = repo.findById(productId).get();
		
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Android 10");
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}

}
