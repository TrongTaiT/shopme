package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

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
		try {
			Category category = categoryService.getCategory(alias);

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

			return "product/products_by_category";
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
			return "error/404";
		}
	}

	@GetMapping("/p/{product_alias}")
	public String viewProductDetail( //
			Model model, //
			@PathVariable("product_alias") String alias) //
	{
		try {
			Product product = productService.getProduct(alias);

			List<Category> listCategoryParents = //
					categoryService.getCategoryParents(product.getCategory());

			model.addAttribute("product", product);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("pageTitle", product.getShortName());

			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchFirstPage( //
			Model model, //
			@Param("keyword") String keyword) //
	{
		return searchByPage(model, keyword, 1);
	}

	@GetMapping("/search/{pageNum}")
	public String searchByPage( //
			Model model, //
			@Param("keyword") String keyword, //
			@PathVariable("pageNum") int pageNum) //
	{
		Page<Product> pageProducts = productService.search(keyword, pageNum);		
		List<Product> listResult = pageProducts.getContent();

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

		model.addAttribute("keyword", keyword);
		model.addAttribute("listResult", listResult);
		model.addAttribute("pageTitle", keyword + " - Search Result");

		return "product/search_result";
	}

}
