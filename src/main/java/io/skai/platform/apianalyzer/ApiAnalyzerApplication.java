package io.skai.platform.apianalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAnalyzerApplication.class, args);

		/*
		 * !!! Consider the scalability of input data !!!
		 *
		 * [*] Add configuration for most recent urls
		 * [*] Implement file parser
		 * [*] Validate each row's field
		 * [*] Implement output printer
		 * [*] Implement logic for data analyzation
		 * [_] Cover it by the tests
		 * [_] Add Swagger
		 */
	}
}
