package br.com.alura.servico_reserva.controller;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("reservadb")
                    .withUsername("root")
                    .withPassword("root");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.r2dbc.url",
                () -> String.format(
                        "r2dbc:mysql://%s:%d/%s",
                        mysql.getHost(),
                        mysql.getFirstMappedPort(),
                        mysql.getDatabaseName()));

        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);

        registry.add(
                "spring.flyway.url",
                mysql::getJdbcUrl);

        registry.add(
                "spring.flyway.user",
                mysql::getUsername);

        registry.add(
                "spring.flyway.password",
                mysql::getPassword);
    }

}
