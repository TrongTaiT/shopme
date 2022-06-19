package com.shopme.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Brand;

@Repository
public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	public Brand findByName(String name);

	public Long countById(Integer id);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> search(String keyword, Pageable pageable);

}
