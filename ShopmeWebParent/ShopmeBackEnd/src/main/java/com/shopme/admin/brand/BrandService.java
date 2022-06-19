package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	public static final int BRAND_PER_PAGE = 10;

	@Autowired
	private BrandRepository repo;
	
	public Page<Brand> listByPage(int pageNum, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.search(keyword, pageable);
		}
		
		return repo.findAll(pageable);
	}

	public List<Brand> listAll() {
		return (List<Brand>) repo.findAll();
	}

	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with Id " + id);
		}
	}

	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with Id " + id);
		}

		repo.deleteById(id);
	}

	public boolean isNameUnique(Integer id, String name) {
		Brand brandInDB = repo.findByName(name);

		if (brandInDB == null)
			return true;

		if (id != null) {
			// updating the same brand
			if (id == brandInDB.getId()) {
				return true;
			}
		}

		return false;
	}

}
