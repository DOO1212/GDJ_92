package com.winter.app.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class FileConfig {

	// application.properties에서 설정한 파일 저장 경로를 주입받습니다. 예: D:/upload/
	@Value("${app.upload}")
	private String path;

	// application.properties에서 설정한 URL 경로를 주입받습니다. 예: /files/
	@Value("${app.url}")
	private String url;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// '/files/**' 패턴의 URL 요청이 오면
		registry
			.addResourceHandler(url)
			// 로컬 디스크의 'D:/upload/' 경로와 매핑합니다.
			.addResourceLocations("file:\\"+path)
			;
	}

}
