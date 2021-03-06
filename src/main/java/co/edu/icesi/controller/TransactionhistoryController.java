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
import co.edu.icesi.model.Product;
import co.edu.icesi.model.Transactionhistory;
import co.edu.icesi.repositories.ProductRepositoryInterface;
import co.edu.icesi.repositories.TransactionHistoryRepositoryInterface;
import co.edu.icesi.services.ProductService;
import co.edu.icesi.services.TransactionHistoryService;

@Controller
@RequestMapping("/transaction-histories")
public class TransactionhistoryController {

	private TransactionHistoryRepositoryInterface transactionhistoryRepository;
	private TransactionHistoryService transactionhistoryService;
	private ProductService productService;
	private ProductRepositoryInterface productRepository;
	
	@Autowired
	BusinessDelegate delegate;

	@Autowired
	public TransactionhistoryController(TransactionHistoryRepositoryInterface transactionhistoryRepository,
			TransactionHistoryService transactionhistoryService, ProductRepositoryInterface productRepository,
			ProductService productService) {
		this.transactionhistoryRepository = transactionhistoryRepository;
		this.transactionhistoryService = transactionhistoryService;
		this.productRepository = productRepository;
		this.productService = productService;
	}

	@GetMapping("")
	public String transactionhistoryIndex(Model model) {
		model.addAttribute("transactionalhistories", delegate.showTransactionhistoryList());
		return "/transaction-histories/index";
	}

	@GetMapping("/edit/{id}")
	public String editTransactionhistory(Model model, @PathVariable("id") Integer id) {

		model.addAttribute("trh", delegate.getTransactionhistory(id));
		model.addAttribute("products", delegate.showProductList());
		return "transaction-histories/edit";
	}

	@PostMapping("/edit/{id}")
	public String postEditTransactionhistory(@ModelAttribute("trh") @Validated(Add.class) Transactionhistory trh,
			BindingResult result, Model model, @PathVariable("id") Integer id,
			@RequestParam(value = "action", required = true) String action) {

		if (!action.equals("Cancel")) {
			
			if (result.hasErrors()) {
				model.addAttribute("products", delegate.showProductList());

				return "transaction-histories/edit";
			}
			trh.setProduct(trh.getProduct());
//			transactionhistoryService.editCorrect(trh, trh.getProduct().getProductid());
			delegate.editTransactionhistory(id, trh);
		}
		return "redirect:/transaction-histories";
	}

	@GetMapping("/add")
	public String addTransactionhistory(Model model) {
		model.addAttribute("trh", new Transactionhistory());
		model.addAttribute("products", delegate.showProductList());

		return "/transaction-histories/add";
	}

	@PostMapping("/add")
	public String addTransactionhistoryPost(@ModelAttribute("trh") @Validated(Add.class) Transactionhistory trh,
			BindingResult result, @RequestParam(value = "action", required = true) String action, Model model) {

		if (!action.equals("Cancel")) {
			if (result.hasErrors()) {
				model.addAttribute("products", delegate.showProductList());
				return "/transaction-histories/add";
			}
			trh.setProduct(trh.getProduct());
//			transactionhistoryService.saveCorrect(trh, trh.getProduct().getProductid());
			delegate.addTransactionhistory(trh);
		}
		return "redirect:/transaction-histories";
	}

	@GetMapping("/delete/{id}")
	public String deleteTransactionhistory(@PathVariable("id") Integer id, Model model) {
		Transactionhistory trh =delegate.getTransactionhistory(id);
		delegate.deleteTransactionhistory(trh);
//		transactionhistoryService.delete(trh);
		model.addAttribute("transactionhistories", delegate.showTransactionhistoryList());
		return "redirect:/transaction-histories";
	}
	

	@GetMapping("/{id}")
	public String getProduct(Model model, @PathVariable("id") Integer id) {
		Transactionhistory trh = delegate.getTransactionhistory(id);

		model.addAttribute("trh", trh);

		return "transaction-histories/information";
	}

}
