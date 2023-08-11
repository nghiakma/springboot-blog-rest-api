package com.springboot.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App REST APIs",
				description = "Spring Boot Blog App REST APIs Documention",
				version = "v1.0",
				contact = @Contact(
						name = "nghia",
						email = "nghiangua@gmail.com",
						url="https://www.javaguides.net"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.javaguides.net/license"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog App Documentation",
				url = "https://github.com/nghiakma/springboot-blog-rest-api"
		)
)
public class SpringbootBlogRestApiApplication {

	//danh dau tren method thong bao cho spring method do sinh ra mot bean va dc quan ly boi spring container
	//tiem vao cac lop can dung toi no
	@Bean
    public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

}
