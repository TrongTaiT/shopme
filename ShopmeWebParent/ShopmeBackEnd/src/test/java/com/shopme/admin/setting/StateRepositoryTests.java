package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired
	private StateRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateState1() {
		Country vn = entityManager.find(Country.class, 3);
		State state = new State("Lâm Đồng", vn);

		State savedState = repo.save(state);

		assertThat(savedState.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateState2() {
		Country us = entityManager.find(Country.class, 4);
		State state = new State("California", us);

		State savedState = repo.save(state);

		assertThat(savedState.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateState3() {
		Country us = entityManager.find(Country.class, 4);
		State state = new State("Virginia", us);

		State savedState = repo.save(state);

		assertThat(savedState.getId()).isGreaterThan(0);
	}

	@Test
	public void testListStatesByCountry() {
		Country us = entityManager.find(Country.class, 4);

		List<State> listStates = repo.findByCountryOrderByNameAsc(us);

		listStates.forEach(System.out::println);

		assertThat(listStates.size()).isGreaterThan(0);
	}

	@Test
	public void testGetState() {
		Integer id = 2;
		Optional<State> result = repo.findById(id);

		assertThat(result).isNotEmpty();
	}

	@Test
	public void testUpdateState() {
		Integer id = 2;
		String newName = "Alabama";

		State state = repo.findById(id).get();
		state.setName(newName);

		State updatedState = repo.save(state);

		assertThat(updatedState.getName()).isEqualTo(newName);
	}

	@Test
	public void testDeleteState() {
		Integer id = 1;
		repo.deleteById(id);

		Optional<State> result = repo.findById(id);

		assertThat(result).isEmpty();
	}

}
