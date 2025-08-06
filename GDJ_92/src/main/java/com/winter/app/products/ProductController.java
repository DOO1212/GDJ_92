package com.winter.app.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/products/*")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("list")
	public void list(Model model)throws Exception{
		
		model.addAttribute("list", productService.list());
	}
	
	@GetMapping("detail")
	public void detail(ProductVO productVO, Model model)throws Exception{
		 model.addAttribute("vo", productService.detail(productVO));
	}
	
	@GetMapping("add")
	public String add()throws Exception{
		
		return "products/product_form";
	}
	
	@PostMapping("add")
	public ModelAndView add(ProductVO productVO, Model model)throws Exception{
		int result=productService.insert(productVO);
		
		String msg="상품 등록 실패";
		if(result>0) {
			msg= "상품등록 성공";
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", msg);
		mv.addObject("url", "./list");
		
		mv.setViewName("commons/result");
		
//		model.addAttribute("msg", msg);
//		model.addAttribute("url", "./list");
		
		return mv;
	}
	
	@GetMapping("update")
	public ModelAndView update(ProductVO productVO, ModelAndView mv)throws Exception{
		productVO = productService.detail(productVO);
		//model.addAttribute("vo", productVO);
		mv.addObject("vo", productVO);
		mv.setViewName("products/product_form");
		return mv;
	}

	@PostMapping("update")
	public String update(ProductVO productVO, Model model)throws Exception{
		int result = productService.update(productVO);
		String msg="상품 수정 실패";
		if(result>0) {
			msg= "상품 수정 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./detail?productNum="+productVO.getProductNum());
		return "commons/result";
	}
	
	@PostMapping("delete")
	public String delete(ProductVO productVO, Model model)throws Exception{
		int result = productService.delete(productVO);
		String msg="상품 삭제 실패";
		if(result>0) {
			msg= "상품 삭제 성공";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./list");
		return "commons/result";
	}	
	
}
