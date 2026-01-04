package ru.kata.spring.boot_security.demo.configs;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {
    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        // Create roles
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        if (roleService.findByName("ROLE_ADMIN") == null) {
            roleService.saveRole(adminRole);
        }
        if (roleService.findByName("ROLE_USER") == null) {
            roleService.saveRole(userRole);
        }

        // Create admin user
        if (userService.findByUsername("admin") == null) {
            User admin = new User("admin", "admin", "admin", "admin", "admin@mail.ru", 30);
            Set<Long> adminRoleIds = new HashSet<>();
            adminRoleIds.add(roleService.findByName("ROLE_ADMIN").getId());
            adminRoleIds.add(roleService.findByName("ROLE_USER").getId());
            userService.saveUser(admin, adminRoleIds);
        }

        // Create regular user
        if (userService.findByUsername("user") == null) {
            User user = new User("user", "user", "user", "user", "user@mail.ru", 25);
            Set<Long> userRoleIds = new HashSet<>();
            userRoleIds.add(roleService.findByName("ROLE_USER").getId());
            userService.saveUser(user, userRoleIds);
        }
    }
}

