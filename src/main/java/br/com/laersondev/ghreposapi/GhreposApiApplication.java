package br.com.laersondev.ghreposapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class GhreposApiApplication {

	public static void main(final String[] args) {
		SpringApplication.run(GhreposApiApplication.class, args);
	}

	@Bean
	public RestTemplate buildRestTemplate(final RestTemplateBuilder builder) {
		return builder.build();
	}

}
