package ru.kata.spring.boot_security.demo.dto;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserUpdateDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    private Long[] selectedRoles;

    public UserUpdateDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long[] getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(Long[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Set<Long> getSelectedRolesAsSet() {
        if (selectedRoles == null) {
            return null;
        }
        return Stream.of(selectedRoles).collect(Collectors.toSet());
    }
}

