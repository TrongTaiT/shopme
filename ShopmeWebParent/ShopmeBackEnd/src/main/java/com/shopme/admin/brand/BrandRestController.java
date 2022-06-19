package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService service;

	@PostMapping("/brands/check_name_unique")
	public String checkDuplicateName( //
			@Param("id") Integer id, //
			@Param("name") String name) //
	{
		return service.isNameUnique(id, name) ? "OK" : "Duplicated";
	}

	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand( //
			@PathVariable(name = "id") Integer brandId, //
			RedirectAttributes ra) //
			throws BrandNotFoundRestException //
	{
		List<CategoryDTO> listCategories = new ArrayList<>();
		try {
			Brand brand = service.get(brandId);
			Set<Category> categories = brand.getCategories();
			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}
			return listCategories;
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			throw new BrandNotFoundRestException();
		}
	}

}
