package com.example.shopapp.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.shopapp.models.Role;
import com.example.shopapp.repositories.RoleRepository;

@Configuration
public class DatabaseInitConfig {

    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository, JdbcTemplate jdbcTemplate) {
        return args -> {
            if (roleRepository.count() == 0) {
                try {
                    jdbcTemplate.execute("INSERT INTO roles (id, name) VALUES (1, '" + Role.ADMIN + "')");
                    jdbcTemplate.execute("INSERT INTO roles (id, name) VALUES (2, '" + Role.USER + "')");
                    System.out.println("====== AUTO INSERTED ROLES INTO DATABASE ======");
                } catch (Exception e) {
                    System.out.println("====== COULD NOT INSERT ROLES: " + e.getMessage() + " ======");
                }
            }
        };
    }
}
