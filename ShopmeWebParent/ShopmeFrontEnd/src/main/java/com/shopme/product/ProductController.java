package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage( //
			Model model, //
			@PathVariable("category_alias") String alias) //
	{
		return viewCategoryByPage(model, alias, 1);
	}

	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage( //
			Model model, //
			@PathVariable("category_alias") String alias, //
			@PathVariable("pageNum") int pageNum) //
	{
		Category category = categoryService.getCategory(alias);

		if (category == null) {
			return "error/404";
		}

		List<Category> listCategoryParents = categoryService.getCategoryParents(category);

		Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
		List<Product> listProducts = pageProducts.getContent();
		
		long countStart = ProductService.PRODUCT_PER_PAGE * pageNum + 1;
		long endCount = countStart + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("countStart", countStart);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategoryParents", listCategoryParents);
		model.addAttribute("category", category);
		model.addAttribute("pageTitle", category.getName());

		return "products_by_category";
	}

}
