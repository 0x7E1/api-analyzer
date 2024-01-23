package io.skai.platform.apianalyzer;

import io.skai.platform.apianalyzer.service.impl.ConsoleAnalyzationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApiAnalyzerApplication {
	static ConfigurableApplicationContext context;
	static boolean isEmbeddedEnabled;

	public static void main(String[] args) {
		context = SpringApplication.run(ApiAnalyzerApplication.class, args);

		if (isEmbeddedEnabled) {
			context.getBean(ConsoleAnalyzationService.class)
				.run(context);
		}
	}

	@Value("${analyzer.embedded-mode.enabled}")
	public void initIsEmbeddedEnabled(boolean value) {
		isEmbeddedEnabled = value;
	}
}
