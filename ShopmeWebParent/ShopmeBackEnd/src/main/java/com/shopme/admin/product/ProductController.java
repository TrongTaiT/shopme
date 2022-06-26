package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(model, 1, "name", "asc", null, 0);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage( //
			Model model, //
			@PathVariable("pageNum") int pageNum, //
			@RequestParam(name = "sortField", required = false) String sortField, //
			@RequestParam(name = "sortDir", required = false) String sortDir, //
			@RequestParam(name = "keyword", required = false) String keyword, //
			@RequestParam(name = "categoryId", required = false) Integer categoryId) //
	{
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, //
				keyword, categoryId);

		List<Product> listProducts = page.getContent();

		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		long startCount = ProductService.PRODUCTS_PER_PAGE * (pageNum - 1) + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("moduleURL", "/products");

		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct( //
			Model model, //
			Product product, //
			RedirectAttributes ra, //
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart, //
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts, //
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs, //
			@RequestParam(name = "detailNames", required = false) String[] detailNames, //
			@RequestParam(name = "detailValues", required = false) String[] detailValues, //
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs, //
			@RequestParam(name = "imageNames", required = false) String[] imageNames, //
			@AuthenticationPrincipal ShopmeUserDetails loggedUser) //
			throws IOException //
	{
		if (loggedUser.hasRole("Salesperson")) {
			try {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "The product has been saved successfully.");
				return "redirect:/products";
			} catch (ProductNotFoundException e) {
				ra.addFlashAttribute("message", e.getMessage());
				return "redirect:/products";
			}
		}

		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		ProductSaveHelper.savedUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);

		ra.addFlashAttribute("message", "The product has been saved successfully.");
		return "redirect:/products";
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus( //
			@PathVariable("id") Integer id, //
			@PathVariable("status") Boolean enabled, //
			RedirectAttributes ra) //
	{
		productService.updateProductEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";
		String message = "The product with ID " + id + " has been " + status;

		ra.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct( //
			@PathVariable("id") Integer id, //
			RedirectAttributes ra) //
	{
		try {
			productService.delete(id);

			String productExtraImagesDir = "../product-images/" + id + "/extras";
			FileUploadUtil.removeDir(productExtraImagesDir);
			String productImagesDir = "../product-images/" + id;
			FileUploadUtil.removeDir(productImagesDir);

			ra.addFlashAttribute("message", "The product with ID " + id + " has been deleted successfully.");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct( //
			@PathVariable("id") Integer id, //
			Model model, //
			RedirectAttributes ra) //
	{
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("listBrands", listBrands);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails( //
			@PathVariable("id") Integer id, //
			Model model, //
			RedirectAttributes ra) //
	{
		try {
			Product product = productService.get(id);

			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

}
