package co.edu.icesi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.businessdelegate.BusinessDelegate;
import co.edu.icesi.model.Add;
import co.edu.icesi.model.Productreview;
import co.edu.icesi.model.Transactionhistory;
import co.edu.icesi.services.ProductService;
import co.edu.icesi.services.ProductreviewService;

@Controller
@RequestMapping("/product-reviews")
public class ProductreviewController {

	@Autowired
	BusinessDelegate delegate;

	private ProductService productService;
	private ProductreviewService productreviewService;

	@Autowired
	public ProductreviewController(ProductService productService, ProductreviewService productreviewService) {
		this.productService = productService;
		this.productreviewService = productreviewService;
	}

	@GetMapping("")
	public String productreviewIndex(Model model) {
		model.addAttribute("productreviews", delegate.showProductreviewList());
		return "/product-reviews/index";
	}

	@GetMapping("/edit/{id}")
	public String editProductreview(Model model, @PathVariable("id") Integer id) {
		model.addAttribute("productreview", delegate.getProductreview(id));
		model.addAttribute("products", delegate.showProductList());
		return "/product-reviews/edit";
	}

	@PostMapping("/edit/{id}")
	public String postEditProductreview(
			@ModelAttribute("productreview") @Validated(Add.class) Productreview productreview, BindingResult result,
			Model model, @PathVariable("id") Integer id,
			@RequestParam(value = "action", required = true) String action) {

		if (!action.equals("Cancel")) {

			if (result.hasErrors()) {
				model.addAttribute("products", delegate.showProductList());

				return "product-reviews/edit";
			}
			productreview.setProduct(productreview.getProduct());
			delegate.editProductreview(id, productreview);
//			productreviewService.editProductreview(productreview, productreview.getProduct().getProductid());
		}
		return "redirect:/product-reviews";
	}

	@GetMapping("/add")
	public String addProductreview(Model model) {
		model.addAttribute("productreview", new Productreview());
		model.addAttribute("products", delegate.showProductList());

		return "/product-reviews/add";
	}

	@PostMapping("/add")
	public String addProductreviewPost(
			@ModelAttribute("productreview") @Validated(Add.class) Productreview productreview, BindingResult result,
			@RequestParam(value = "action", required = true) String action, Model model) {

		if (!action.equals("Cancel")) {
			if (result.hasErrors()) {
				model.addAttribute("products", delegate.showProductList());
				return "/product-reviews/add";
			}
			productreview.setProduct(productreview.getProduct());
			delegate.addProductreview(productreview);
//			productreviewService.saveProductreview(productreview, productreview.getProduct().getProductid());
		}
		return "redirect:/product-reviews";
	}

	@GetMapping("/delete/{id}")
	public String deleteProductreview(@PathVariable("id") Integer id, Model model) {
		Productreview productreview = delegate.getProductreview(id);
		delegate.deleteProductreview(productreview);
//		productreviewService.delete(productreview);
		model.addAttribute("productreviews", delegate.showProductreviewList());
		return "redirect:/product-reviews";
	}

	@GetMapping("/{id}")
	public String getProduct(Model model, @PathVariable("id") Integer id) {
		Productreview productreview = delegate.getProductreview(id);

		model.addAttribute("productreview", productreview);

		return "product-reviews/information";
	}

}
