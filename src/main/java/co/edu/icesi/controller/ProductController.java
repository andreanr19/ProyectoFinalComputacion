package co.edu.icesi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.icesi.businessdelegate.BusinessDelegate;
import co.edu.icesi.model.Add;
import co.edu.icesi.model.Product;
import co.edu.icesi.services.ProductCategoryService;
import co.edu.icesi.services.ProductService;
import co.edu.icesi.services.ProductSubcategoryService;
import co.edu.icesi.services.UnitMeasureService;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/products")
@Log4j2
public class ProductController {

	@Autowired
	BusinessDelegate delegate;
	
//	private ProductService productService;
//	private ProductCategoryService productcategoryService;
//	private ProductSubcategoryService productsubcategoryService;
//	private UnitMeasureService unitmeasureService;

//	@Autowired
//	public ProductController(ProductService productService, ProductCategoryService productcategoryService,
//			ProductSubcategoryService productsubcategoryService, UnitMeasureService unitmeasureService) {
//		this.productService = productService;
//		this.productcategoryService = productcategoryService;
//		this.productsubcategoryService = productsubcategoryService;
//		this.unitmeasureService = unitmeasureService;
//	}

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("products", delegate.showProductList());
		return "/products/index";
	}

	@GetMapping("/edit/{id}")
	public String editProduct(Model model, @PathVariable("id") Integer id) {
		Product product = delegate.getProduct(id);

		model.addAttribute("product", product);
		model.addAttribute("productcategories", delegate.showProductcategoryList());
		model.addAttribute("productsubcategories", delegate.showProductsubcategoryList());
		model.addAttribute("unitmeasures", delegate.showUnitmeasureList());
		return "products/edit";
	}

	@PostMapping("/edit/{id}")
	public String postEditProduct(@ModelAttribute("product") @Validated(Add.class) Product product,
			BindingResult result, Model model, @PathVariable("id") Integer id,
			@RequestParam(value = "action", required = true) String action) {

		if (!action.equals("Cancel")) {
			if(!product.getSellstartdate().before(product.getSellenddate())) {
				log.info("ererererererer");
				result.addError(new ObjectError("sellstartdate", "Sell start date should be before sell end date"));
			}
			if (result.hasErrors()) {
				
				model.addAttribute("productcategories", delegate.showProductcategoryList());
				model.addAttribute("productsubcategories", delegate.showProductsubcategoryList());
				model.addAttribute("unitmeasures", delegate.showUnitmeasureList());
				return "products/edit";
			}
			
			delegate.editProduct(id,product);
		}
		return "redirect:/products";
	}

	@GetMapping("/add")
	public String addProduct(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("productcategories", delegate.showProductcategoryList());
		model.addAttribute("productsubcategories", delegate.showProductsubcategoryList());
		model.addAttribute("unitmeasures", delegate.showUnitmeasureList());

		return "/products/add";
	}

	@PostMapping("/add")
	public String addProduct(@ModelAttribute("product") @Validated(Add.class) Product product, BindingResult result,
			@RequestParam(value = "action", required = true) String action, Model model) {

		if (!action.equals("Cancel")) {
			if (result.hasErrors()) {
				model.addAttribute("productcategories", delegate.showProductcategoryList());
				model.addAttribute("productsubcategories", delegate.showProductsubcategoryList());
				model.addAttribute("unitmeasures", delegate.showUnitmeasureList());
				return "/products/add";

			}
			delegate.addProduct(product);

//			productService.saveCorrect(product,
//					product.getProductsubcategory().getProductcategory().getProductcategoryid(),
//					product.getProductsubcategory().getProductsubcategoryid(),
//					product.getUnitmeasure1().getUnitmeasurecode());
			
			log.info("HOLAAAA");
		}
		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model) {
		Product product = delegate.getProduct(id);
		delegate.deleteProduct(product);
		model.addAttribute("products", delegate.showProductList());
		return "products/index";
	}

	@GetMapping("/{id}")
	public String getProduct(Model model, @PathVariable("id") Integer id) {
		Product product = delegate.getProduct(id);

		model.addAttribute("product", product);

		return "products/information";
	}

}
