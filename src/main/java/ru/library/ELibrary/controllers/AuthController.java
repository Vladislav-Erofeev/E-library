package ru.library.ELibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.PeopleRegistrationService;
import ru.library.ELibrary.utils.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PeopleRegistrationService registrationService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(PeopleRegistrationService registrationService,
                          PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
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
    public String registration(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "registration";
        registrationService.registrate(person);
        return "login";
    }
}
