package com.shopme.admin.order;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Order;
import com.shopme.common.exception.OrderNotFoundException;

@Service
public class OrderService {

	private static final int ORDER_PER_PAGE = 10;

	@Autowired
	private OrderRepository repo;

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();

		Sort sort = null;

		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDER_PER_PAGE, sort);

		Page<Order> page = null;

		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		} else {
			page = repo.findAll(pageable);
		}

		helper.updateModelAttributes(pageNum, page);
	}

	public Order get(Integer orderId) throws OrderNotFoundException {
		try {
			return repo.findById(orderId).get();
		} catch (NoSuchElementException e) {
			throw new OrderNotFoundException("Could not find any order with ID " + orderId);
		}
	}

	public void delete(Integer orderId) throws OrderNotFoundException {
		Long countById = repo.countById(orderId);

		if (countById == null || countById == 0) {
			throw new OrderNotFoundException("Could not find any order with ID " + orderId);
		}

		repo.deleteById(orderId);
	}

}
