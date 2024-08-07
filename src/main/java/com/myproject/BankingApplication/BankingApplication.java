package com.myproject.BankingApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Banking Application",
				description = "backend REST api with basic CRUD operations",
				version = "v1.0.0",
				contact = @Contact(
						name = "kk",
						email = "kartikayshukla3305@gmail.com",
						url = ""
				),
				license = @License(
						name = "my personal project",
						url = ""
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "banking app documentation",
				url = ""
		)
)

public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
