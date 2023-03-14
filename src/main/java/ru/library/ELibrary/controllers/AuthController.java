package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.PeopleRepository;
import ru.library.ELibrary.services.PeopleRegistrationService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PeopleRegistrationService registrationService;

    @Autowired
    public AuthController(PeopleRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person")Person person) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) {
        registrationService.registrate(person);
        return "login";
    }
}
