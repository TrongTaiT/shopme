package com.shopme.admin.setting;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Setting;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

}
