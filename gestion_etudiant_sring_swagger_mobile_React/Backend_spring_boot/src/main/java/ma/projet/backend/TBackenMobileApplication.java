package ma.projet.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "ma.projet.entities")
@EnableJpaRepositories(basePackages = "ma.projet.repisitories")
@ComponentScan(basePackages = {"ma.projet.controllers","ma.projet.services"})
public class TBackenMobileApplication {

	public static void main(String[] args) {
		SpringApplication.run(TBackenMobileApplication.class, args);
	}

}
