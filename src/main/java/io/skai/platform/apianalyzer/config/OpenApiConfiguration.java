package io.skai.platform.apianalyzer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        var contact = new Contact();
        contact.setName("Vladyslav Volkov");
        contact.setEmail("vladyslaw.volkov@gmail.com");
        contact.setUrl("https://t.me/not_Uncle_Petro");

        var info = new Info();
        info.setTitle("API Load Analyzer");
        info.setVersion("1.0.1");
        info.setDescription("Open API documentation for skai.io test task");
        info.setContact(contact);

        return new OpenAPI()
            .info(info);
    }
}
