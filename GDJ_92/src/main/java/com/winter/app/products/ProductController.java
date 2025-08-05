package com.winter.app.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("products/*")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("list")
	public void list(Model model) throws Exception {
		model.addAttribute("list", productService.list());
	}

	@GetMapping("detail")
	public void detail(Model model, ProductVO productVO) throws Exception {
		model.addAttribute("vo", productService.detail(productVO));

	}

	@GetMapping("add")
	public String add() throws Exception {
		return "products/product_form";
	}

//	@PostMapping("add")
//	public String add() throws Exception {

//	}

	@GetMapping("update")
	public String update() throws Exception {
		return "products/product_form";
	}

//	@PostMapping("update")
//	public String update() throws Exception {
//		
//	}

//	@PostMapping("delete")
//	public String delete() throws Exception {
//		
//	}

}
