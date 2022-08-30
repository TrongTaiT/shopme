package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository repo;

	@Test
	public void testCreateCustomer1() {
		Customer customer = new Customer();
		customer.setEmail("nguyentrongtai.ait@gmail.com");
		customer.setPassword("trongtai");
		customer.setFirstName("Tai");
		customer.setLastName("Nguyen");
		customer.setPhoneNumber("0784224333");
		customer.setAddressLine1("12 Quang Trung");
		customer.setCity("Ho Chi Minh city");
		customer.setState("Ho Chi Minh city");
		customer.setCountry(new Country(242));
		customer.setPostalCode("70000");
		customer.setEnabled(false);

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateCustomer2() {
		Customer customer = new Customer();
		customer.setEmail("nguyentruongchinh.dev@gmail.com");
		customer.setPassword("truongchinh");
		customer.setFirstName("Chinh");
		customer.setLastName("Nguyen");
		customer.setPhoneNumber("0909215487");
		customer.setAddressLine1("37 Le Thi Nho");
		customer.setCity("Ho Chi Minh city");
		customer.setState("Ho Chi Minh city");
		customer.setCountry(new Country(242));
		customer.setPostalCode("70000");
		customer.setEnabled(false);

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAll() {
		Iterable<Customer> iterable = repo.findAll();

		iterable.forEach(System.out::println);

		assertThat(iterable).size().isGreaterThan(0);
	}

	@Test
	public void testFindById() {
		Integer id = 1;
		Optional<Customer> findById = repo.findById(id);

		assertThat(findById).isPresent();
	}

	@Test
	public void testUpdateCustomer() {
		Integer id = 1;

		Customer customer = repo.findById(id).get();
		customer.setEmail("nguyentrongtai@gmail.com");
		customer.setPassword("trongtai6");
		customer.setFirstName("Tai");
		customer.setLastName("Nguyen");
		customer.setPhoneNumber("0784524338");
		customer.setAddressLine1("1300 Quang Trung");
		customer.setCity("Ho Chi Minh city");
		customer.setState("Ho Chi Minh city");
		customer.setCountry(new Country(242));
		customer.setPostalCode("70000");
		customer.setEnabled(false);
		customer.setVerificationCode("trongtaipro");

		Customer savedCustomer = repo.save(customer);

		Customer customerInDB = repo.findById(id).get();

		assertThat(savedCustomer).isEqualTo(customerInDB);
	}

	@Test
	public void testFinByEmail() {
		String email = "nguyentrongtai@gmail.com";

		Customer findByEmail = repo.findByEmail(email);

		assertThat(findByEmail).isNotNull();
	}

	@Test
	public void testFindByVerificationCode() {
		String verificationCode = "trongtaipro";

		Customer findByVerificationCode = repo.findByVerificationCode(verificationCode);

		assertThat(findByVerificationCode).isNotNull();
	}

	@Test
	public void testUpdateEnable() {
		Integer id = 1;

		repo.enable(id);

		Customer customer = repo.findById(id).get();

		assertThat(customer.getEnabled()).isTrue();
	}

	@Test
	public void testDeleteCustomer() {
		Integer id = 2;
		repo.deleteById(id);

		Optional<Customer> findById = repo.findById(id);

		assertThat(findById).isEmpty();
	}

	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		repo.enable(customerId);

		Customer customer = repo.findById(customerId).get();

		assertThat(customer.getEnabled()).isTrue();
	}

	@Test
	public void testUpdateAuthenticationType() {
		Integer id = 1;
		repo.updateAuthenticationType(id, AuthenticationType.DATABASE);

		Customer customer = repo.findById(id).get();
		assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	}

}
