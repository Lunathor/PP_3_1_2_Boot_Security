package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().toString().equals("anonymousUser")) {
            String username = auth.getName();
            ru.kata.spring.boot_security.demo.model.User currentUser = userService.findByUsername(username);
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        }
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().toString().equals("anonymousUser")) {
            String username = auth.getName();
            ru.kata.spring.boot_security.demo.model.User currentUser = userService.findByUsername(username);
            if (currentUser != null) {
                model.addAttribute("currentUser", currentUser);
            }
        }
        return "new";
    }

    @PostMapping
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "selectedRoles", required = false) Long[] selectedRoles) {
        try {
            Set<Role> roles = new HashSet<>();
            if (selectedRoles != null && selectedRoles.length > 0) {
                for (Long roleId : selectedRoles) {
                    Role role = roleService.getRoleById(roleId);
                    if (role != null) {
                        roles.add(role);
                    }
                }
            }
            // Если роли не выбраны, устанавливаем роль USER по умолчанию
            if (roles.isEmpty()) {
                Role userRole = roleService.findByName("ROLE_USER");
                if (userRole != null) {
                    roles.add(userRole);
                }
            }
            user.setRoles(roles);
            userService.saveUser(user);
            return "redirect:/admin";
        } catch (Exception e) {
            // Логируем ошибку для отладки
            e.printStackTrace();
            return "redirect:/admin/new?error=true";
        }
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam(value = "firstName") String firstName,
                             @RequestParam(value = "lastName") String lastName,
                             @RequestParam(value = "age") Integer age,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "selectedRoles", required = false) Long[] selectedRoles) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAge(age);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            
            Set<Role> roles = new HashSet<>();
            if (selectedRoles != null) {
                for (Long roleId : selectedRoles) {
                    roles.add(roleService.getRoleById(roleId));
                }
            }
            user.setRoles(roles);
            userService.updateUser(user);
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}/data")
    @ResponseBody
    public ResponseEntity<User> getUserData(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}

