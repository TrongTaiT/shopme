package com.shopme.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingRepositoryTests {

	@Autowired
	private SettingRepository repo;

	@Test
	public void testFindByTwoCategories() {
		List<Setting> settings = repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		settings.forEach(System.out::println);
	}

}
