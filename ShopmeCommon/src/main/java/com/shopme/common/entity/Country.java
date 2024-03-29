package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends IdBasedEntity {

	@Column(length = 45, nullable = false)
	private String name;

	@Column(length = 5, nullable = false)
	private String code;

	@OneToMany(mappedBy = "country")
	private Set<State> set = new HashSet<>();

	// constructors
	public Country() {
	}

	public Country(Integer id) {
		this.id = id;
	}

	public Country(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	// inherited
	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}

	// getters && setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
