package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

@Controller
public class ProductController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;

	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();

		model.addAttribute("listProducts", listProducts);

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

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct( //
			Model model, //
			Product product, //
			RedirectAttributes ra, //
			@RequestParam("fileImage") MultipartFile mainImageMultipart, //
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts, //
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs, //
			@RequestParam(name = "detailNames", required = false) String[] detailNames, //
			@RequestParam(name = "detailValues", required = false) String[] detailValues, //
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs, //
			@RequestParam(name = "imageNames", required = false) String[] imageNames) //
			throws IOException //
	{
		setMainImageName(mainImageMultipart, product);
		setExistingExtraImageNames(imageIDs, imageNames, product);
		setNewExtraImageNames(extraImageMultiparts, product);
		setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		savedUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

		deleteExtraImagesWeredRemovedOnForm(product);

		ra.addFlashAttribute("message", "The product has been saved successfully.");
		return "redirect:/products";
	}

	private void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);

		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();

				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

	private void setExistingExtraImageNames(String[] imagesIDs, String[] imageNames, Product product) {
		if (imagesIDs == null || imagesIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();
		for (int count = 0; count < imagesIDs.length; count++) {
			Integer id = Integer.parseInt(imagesIDs[count]);
			String name = StringUtils.cleanPath(imageNames[count]);

			images.add(new ProductImage(id, name, product));
		}

		product.setImages(images);
	}

	private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}

	private void savedUploadedImages(MultipartFile mainImageMultipart, //
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException //
	{
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultiparts.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";

			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty())
					continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
	}

	private void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);

			if (id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}
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
