package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "asc", null);
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage( //
			@PathVariable("pageNum") int pageNum, //
			Model model, //
			@Param("sortDir") String sortDir, //
			@Param("keyword") String keyword) //
	{
		Page<Brand> page = brandService.listByPage(pageNum, sortDir, keyword);
		List<Brand> listBrands = page.getContent();

		long startCount = (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRAND_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("sortField", "name");
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveBrand( //
			Brand brand, //
			@RequestParam("fileImage") MultipartFile multipartFile, //
			RedirectAttributes ra) throws IOException //
	{
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("message", "The brand has been saved successfully.");
		return "redirect:/brands";
	}

	@GetMapping("/brands/edit/{id}")
	public String editBrand( //
			Model model, //
			@PathVariable("id") Integer brandId, //
			RedirectAttributes ra) //
	{
		try {
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			Brand brand = brandService.get(brandId);

			model.addAttribute("listCategories", listCategories);
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + brandId + ")");

			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteBrand( //
			@PathVariable("id") Integer id, //
			RedirectAttributes ra) //
	{
		try {
			brandService.delete(id);

			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);

			ra.addFlashAttribute("message", "The brand with ID " + id + " has been deleted successfully.");
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/brands";
	}

}
