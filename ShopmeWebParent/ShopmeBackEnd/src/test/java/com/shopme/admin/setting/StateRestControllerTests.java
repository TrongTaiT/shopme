package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StateRepository repo;

	@Test
	@WithMockUser(username = "nguyentrongtai@gmail.com", password = "hihi", roles = "ADMIN")
	public void testListStateByCountry() throws Exception {
		Integer countryId = 4;
		String url = "/states/list_by_country/" + countryId;

		MvcResult result = mockMvc.perform(get(url))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		State[] states = objectMapper.readValue(jsonResponse, State[].class);

		assertThat(states).hasSizeGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "nguyentrongtai@gmail.com", password = "hihi", roles = "ADMIN")
	public void testCreateState() throws Exception {
		String stateName = "Alabama";
		Integer countryId = 4;
		State state = new State(stateName, new Country(countryId));
		String url = "/states/save";

		MvcResult result = mockMvc.perform(post(url).contentType("application/json")//
				.content(objectMapper.writeValueAsString(state))//
				.with(csrf()))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andReturn();

		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);

		Optional<State> findById = repo.findById(stateId);
		assertThat(findById).isPresent();

		State savedState = repo.findById(stateId).get();
		assertThat(savedState.getName()).isEqualTo(stateName);
		assertThat(savedState.getCountry().getId()).isEqualTo(countryId);
	}

	@Test
	@WithMockUser(username = "nguyentrongtai@gmail.com", password = "hihi", roles = "ADMIN")
	public void testUpdateState() throws Exception {
		Integer stateId = 4;
		String stateName = "Philadelphia";
		Integer countryId = 4;
		State state = new State(stateId, stateName, new Country(countryId));
		String url = "/states/save";

		MvcResult result = mockMvc.perform(post(url).contentType("application/json")//
				.content(objectMapper.writeValueAsString(state))//
				.with(csrf()))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andReturn();

		String response = result.getResponse().getContentAsString();
		Integer savedStateId = Integer.parseInt(response);

		Optional<State> findById = repo.findById(savedStateId);
		assertThat(findById).isPresent();

		State savedState = repo.findById(stateId).get();
		assertThat(savedState.getName()).isEqualTo(stateName);
		assertThat(savedState.getCountry().getId()).isEqualTo(countryId);
	}

	@Test
	@WithMockUser(username = "nguyentrongtai@gmail.com", password = "hihi", roles = "ADMIN")
	public void testDeleteState() throws Exception {
		Integer stateId = 5;
		String url = "/states/delete/" + stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById).isNotPresent();
	}

}