package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

	@Autowired
	private AddressRepository repo;

	@Test
	public void testAddNew1() {
		Customer customer = new Customer(40);
		Country country = new Country(242);

		Address address = new Address();
		address.setFirstName("Tai");
		address.setLastName("Nguyen");
		address.setPhoneNumber("0784224338");
		address.setAddressLine1("thôn Phú Hiệp 1");
		address.setAddressLine2("xã Gia Hiệp, huyện Di Linh");
		address.setCity("Lâm Đồng");
		address.setState("Lâm Đồng");
		address.setPostalCode("66000");
		address.setCustomer(customer);
		address.setCountry(country);
		address.setDefaultForShipping(true);

		Address savedAddress = repo.save(address);

		assertThat(savedAddress.getId()).isGreaterThan(0);
	}

	@Test
	public void testAddNew2() {
		Customer customer = new Customer(40);
		Country country = new Country(242);

		Address address = new Address();
		address.setFirstName("Tai");
		address.setLastName("Nguyen");
		address.setPhoneNumber("0784224338");
		address.setAddressLine1("Phan Huy Ích");
		address.setAddressLine2("phường 12, quận Gò Vấp");
		address.setCity("Ho Chi Minh");
		address.setState("Ho Chi Minh");
		address.setPostalCode("70000");
		address.setCustomer(customer);
		address.setCountry(country);
		address.setDefaultForShipping(true);

		Address savedAddress = repo.save(address);

		assertThat(savedAddress.getId()).isGreaterThan(0);
	}

	@Test
	public void testFindByCustomer() {
		Customer customer = new Customer(40);

		List<Address> list = repo.findByCustomer(customer);
		list.forEach(System.out::println);

		assertThat(list).hasSize(2);
	}

	@Test
	public void testFindByIdAndCustomer() {
		Customer customer = new Customer(40);
		Integer id = 2;

		Address address = repo.findByIdAndCustomer(id, customer);
		System.out.println(address);

		assertThat(address).isNotNull();
	}

	@Test
	public void testUpdate() {
		Customer customer = new Customer(40);
		Country country = new Country(242);
		Integer id = 1;

		Address address = new Address();
		address.setId(id);
		address.setFirstName("Tèo");
		address.setLastName("Téo Teo");
		address.setPhoneNumber("0344818478");
		address.setAddressLine1("Quang Trung");
		address.setAddressLine2("phường 14, quận Gò Vấp");
		address.setCity("Ho Chi Minh");
		address.setState("Ho Chi Minh");
		address.setPostalCode("70000");
		address.setCustomer(customer);
		address.setCountry(country);
		address.setDefaultForShipping(false);

		Address savedAddress = repo.save(address);

		assertThat(savedAddress.getFirstName()).isEqualTo("Tèo");
	}

	@Test
	public void testDeleteByIdAndCustomer() {
		Customer customer = new Customer(40);
		Integer id = 2;

		repo.deleteByIdAndCustomer(id, customer);

		Optional<Address> findById = repo.findById(id);

		assertThat(findById).isEmpty();
	}

	@Test
	public void testSetDefaultAddress() {
		Integer id = 1;

		repo.setDefaultAddress(id);

		Address address = repo.findById(id).get();

		assertThat(address.getDefaultForShipping()).isTrue();
	}

	@Test
	public void testSetNonDefaultForOthers() {
		Integer addressId = 6;
		Integer customerId = 40;

		repo.setDefaultAddress(addressId);
		repo.setNonDefaultForOthers(addressId, customerId);
	}

	@Test
	public void testFindDefaultByCustomer() {
		Integer customerId = 5;
		Address address = repo.findDefaultByCustomer(customerId);

		System.out.println(address);
		
		assertThat(address).isNotNull();
	}

}
