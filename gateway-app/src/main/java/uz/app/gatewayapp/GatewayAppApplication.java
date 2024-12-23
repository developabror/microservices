package uz.app.gatewayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayAppApplication.class, args);
    }

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder rb) {
        return rb.routes()
                .route((path) ->
                        path
                                .path("/auth/**")
                                .uri("lb://auth-app")
                )
                .build();
    }

}
