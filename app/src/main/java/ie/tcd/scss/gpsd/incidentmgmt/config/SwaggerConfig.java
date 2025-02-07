package ie.tcd.scss.gpsd.incidentmgmt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Incident Management API")
                        .version("1.0")
                        .description("API documentation for Incident Management System")
                        .contact(new Contact()
                                .name("Archit Panigrahi")
                                .email("panigraa@tcd.ie")
                                .url("https://incident-mgmt-service.gpsd.scss.tcd.ie")));
    }

}
