package com.shopme.common.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAddressWithCountry extends AbstractAddress {

	@ManyToOne
	@JoinColumn(name = "country_id")
	protected Country country;

	@Override
	public String toString() {
		String address = firstName;
		if (lastName != null && !lastName.isEmpty()) {
			address += " " + lastName;
		}

		if (!addressLine1.isBlank()) {
			address += " || " + addressLine1;
		}

		if (addressLine2 != null && !addressLine2.isBlank()) {
			address += ", " + addressLine2;
		}

		if (!city.isBlank()) {
			address += ", " + city;
		}

		if (!state.isBlank()) {
			address += ", " + state;
		}

		address += ", " + country.getName();

		if (!postalCode.isBlank()) {
			address += ". Postal Code: " + postalCode;
		}

		if (!phoneNumber.isBlank()) {
			address += ". Phone Number: " + phoneNumber;
		}

		return address;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
}
