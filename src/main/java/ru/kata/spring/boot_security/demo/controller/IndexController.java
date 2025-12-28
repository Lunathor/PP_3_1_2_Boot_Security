package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Если пользователь не аутентифицирован, редиректим на /login
        if (authentication == null || authentication.getPrincipal().toString().equals("anonymousUser")) {
            return "redirect:/login";
        }
        
        // Если пользователь аутентифицирован, редиректим на соответствующую страницу
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            return "redirect:/admin";
        } else {
            return "redirect:/user";
        }
    }
}

