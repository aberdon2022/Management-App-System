package org.employee.managementsystem.controller;

import org.employee.managementsystem.UserService;
import org.employee.managementsystem.UserSession;
import org.employee.managementsystem.model.Employee;
import org.employee.managementsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSession userSession;

    public MainController() {
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String handleForm(Model model, @RequestParam String username, @RequestParam String password) {
        try {
            User user = userService.loginUser(username, password);
            userSession.setUser(user);
            return "redirect:/private";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(Model model, @RequestParam String username, @RequestParam String password) {
        User user = userService.registerUser(username, password);
        if (user != null) {
            userSession.setUser(user);
            return "redirect:/private";
        } else {
            model.addAttribute("error", "User already exists");
            model.addAttribute("user", null);
            return "redirect:/register";
        }
    }

    @GetMapping("/private")
    public String privatePage(Model model) {
        User user = userSession.getUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("isAdmin", userSession.isAdmin());
        model.addAttribute("user", user);
        return "private";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "list";
    }

    @GetMapping("/add")
    public String addEmployeePage() {
        return "add";
    }

    @PostMapping("/add")
    public String addEmployee(Model model, Employee employee) {
        userService.addEmployee(employee);
        return "redirect:/list";
    }
}
