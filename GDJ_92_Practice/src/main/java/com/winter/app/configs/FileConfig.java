package com.winter.app.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration: "이 클래스는 Spring의 설정 파일입니다"라고 알려주는 어노테이션입니다.
// Spring Boot가 시작될 때 이 클래스를 읽어서 설정을 적용합니다.
@Configuration
// WebMvcConfigurer 인터페이스를 구현(implements)하여 Spring MVC의 기본 설정을 커스터마이징합니다.
public class FileConfig implements WebMvcConfigurer {
	
	// @Value: application.properties 파일의 속성 값을 주입받는 어노테이션입니다.
	// ${app.upload} 값을 읽어와 path 변수에 저장합니다. (예: "D:/upload/")
	@Value("${app.upload}")
	private String path; // 파일이 실제로 저장된 하드디스크 경로
	
	// ${app.url} 값을 읽어와 url 변수에 저장합니다. (예: "/files/**")
	@Value("${app.url}")
	private String url;  // 브라우저에서 파일에 접근할 때 사용할 URL 경로 패턴
	
	// 정적 리소스(HTML, CSS, JS, 이미지 파일 등)의 경로를 설정하는 메서드입니다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		// registry.addResourceHandler(url):
		// 어떤 URL 패턴의 요청을 처리할지 지정합니다.
		// 여기서는 "/files/**"로 시작하는 모든 요청을 처리하겠다고 설정합니다.
		registry
			.addResourceHandler(url)
			// .addResourceLocations("file:/" + path):
			// 위에서 지정한 URL 요청이 들어왔을 때, 파일을 어디서 찾아 제공할지를 지정합니다.
			// "file:/" 접두사는 로컬 파일 시스템(하드디스크)의 경로를 의미합니다.
			// 최종적으로 "file:/D:/upload/" 경로를 가리키게 됩니다.
				.addResourceLocations("file:" + path);
	}

}