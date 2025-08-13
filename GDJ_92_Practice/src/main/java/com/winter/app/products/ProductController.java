package com.winter.app.products;

// 필요한 클래스들을 가져옵니다.
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
// @RequestMapping: 이 클래스 내부의 모든 메서드들은 "/products/"로 시작하는 URL 요청을 처리합니다.
@RequestMapping("/products/*")
public class ProductController {

	// @Autowired: ProductService 타입의 Bean을 자동으로 주입(DI)합니다.
	@Autowired
	private ProductService productService;

	// GET 방식의 "/products/list" 요청을 처리합니다.
	// 리턴 타입이 void일 경우, Spring은 요청 URL 경로를 기반으로 View의 이름을 자동으로 유추합니다.
	// 즉, "/products/list" 요청은 "products/list"라는 View 이름으로 자동 매핑됩니다.
	@GetMapping("list")
	public void list(Model model) throws Exception {
		// Service를 통해 상품 목록을 가져와서,
		// "list"라는 이름으로 Model에 담아 View로 전달합니다.
		model.addAttribute("list", productService.list());
	}

	// GET 방식의 "/products/detail" 요청을 처리합니다.
	// 리턴 타입이 void이므로, "products/detail"이라는 View 이름으로 자동 매핑됩니다.
	@GetMapping("detail")
	public void detail(ProductVO productVO, Model model) throws Exception {
		// Service를 통해 특정 상품의 상세 정보를 가져와 Model에 담아 전달합니다.
		model.addAttribute("vo", productService.detail(productVO));
	}

	// GET 방식의 "/products/add" 요청을 처리합니다. (상품 등록 폼으로 이동)
	// 리턴 타입이 String일 경우, 반환되는 문자열이 View의 이름이 됩니다.
	@GetMapping("add")
	public String add() throws Exception {
		// "products/product_form" 라는 이름의 View를 직접 지정하여 반환합니다.
		return "products/product_form";
	}

	// POST 방식의 "/products/add" 요청을 처리합니다. (상품 등록 폼 제출)
	// 리턴 타입이 ModelAndView일 경우, 데이터(Model)와 뷰(View) 정보를 함께 담아 반환합니다.
	@PostMapping("add")
	public ModelAndView add(ProductVO productVO) throws Exception {
		// Service를 통해 상품을 DB에 등록합니다.
		int result = productService.insert(productVO);

		// 결과에 따라 메시지를 설정합니다.
		String msg = "상품 등록 실패";
		if (result > 0) {
			msg = "상품등록 성공";
		}

		// 1. ModelAndView 객체를 생성합니다.
		ModelAndView mv = new ModelAndView();
		// 2. addObject 메서드로 View에 전달할 데이터를 담습니다.
		mv.addObject("msg", msg);
		mv.addObject("url", "./list");
		// 3. setViewName 메서드로 보여줄 View의 이름을 설정합니다.
		mv.setViewName("commons/result");

		// 4. 데이터와 뷰 정보가 모두 담긴 ModelAndView 객체를 반환합니다.
		return mv;
	}

	// GET 방식의 "/products/update" 요청을 처리합니다. (상품 수정 폼으로 이동)
	// ModelAndView를 파라미터로 받을 수도 있습니다. Spring이 빈 객체를 만들어 주입해줍니다.
	@GetMapping("update")
	public ModelAndView update(ProductVO productVO, ModelAndView mv) throws Exception {
		// 수정할 상품의 기존 정보를 가져옵니다.
		productVO = productService.detail(productVO);
		// ModelAndView에 데이터를 담고 View 이름을 설정하여 반환합니다.
		mv.addObject("vo", productVO);
		mv.setViewName("products/product_form");
		return mv;
	}

	// POST 방식의 "/products/update" 요청을 처리합니다. (상품 수정 폼 제출)
	@PostMapping("update")
	public String update(ProductVO productVO, Model model) throws Exception {
		int result = productService.update(productVO);
		String msg = "상품 수정 실패";
		if (result > 0) {
			msg = "상품 수정 성공";
		}

		// Model에 데이터를 담고, String으로 View 이름을 반환하는 가장 일반적인 방식입니다.
		model.addAttribute("msg", msg);
		model.addAttribute("url", "./detail?productNum=" + productVO.getProductNum());
		return "commons/result";
	}

	// POST 방식의 "/products/delete" 요청을 처리합니다.
	@PostMapping("delete")
	public String delete(ProductVO productVO, Model model) throws Exception {
		int result = productService.delete(productVO);
		String msg = "상품 삭제 실패";
		if (result > 0) {
			msg = "상품 삭제 성공";
		}

		model.addAttribute("msg", msg);
		model.addAttribute("url", "./list");
		return "commons/result";
	}

}