package com.shopme.common.entity;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.shopme.common.entity.product.Product;

@Entity
@Table(name = "cart_items")
public class CartItem extends IdBasedEntity {

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private int quantity;

	// transient fields
	@Transient
	public float getSubtotal() {
		return product.getDiscountPrice() * quantity;
	}

	// constructors
	public CartItem() {
		super();
	}

	@Override
	public String toString() {
		return "CartItem [customer=" + customer.getFullname() + ", id=" + id //
				+ ", product=" + product.getShortName() + ", quantity=" + quantity + "]";
	}

	// getters && setters
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
