package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "states")
public class State extends IdBasedEntity {

	@Column(length = 45, nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	// constructors
	public State() {
		super();
	}

	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	public State(Integer id, String name, Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	// inherited
	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", country=" + country + "]";
	}

	// getters && setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}
