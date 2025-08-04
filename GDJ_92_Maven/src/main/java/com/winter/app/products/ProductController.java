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
	public String list(Model model) throws Exception {
		List<ProductVO> list = productService.list();
		model.addAttribute("list", list);
		return "products/list";
		
	}
	
//	@GetMapping("detail")
//	public String detail() throws Exception {
//		
//	}
	
//	@GetMapping("add")
//	public String add() throws Exception {
//		
//	}
	
//	@PostMapping("add")
//	public String add() throws Exception {
//		return "products/product_form"
//	}
	
//	@GetMapping("update")
//	public String update() throws Exception {
//		
//	}
	
//	@PostMapping("update")
//	public String update() throws Exception {
//		
//	}
	
//	@PostMapping("delete")
//	public String delete() throws Exception {
//		
//	}

}
