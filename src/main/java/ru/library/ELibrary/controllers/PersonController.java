package ru.library.ELibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.security.PersonDetails;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.PeopleService;
import ru.library.ELibrary.utils.PersonEditValidator;

@Controller
@RequestMapping("/profile")
public class PersonController {
    private final PeopleService peopleService;
    private final PersonEditValidator personValidator;
    private final AuthService authService;

    @Autowired
    public PersonController(PeopleService peopleService, PersonEditValidator personValidator, AuthService authService) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.authService = authService;
    }

    @GetMapping
    public String profilePage(Model model) {
        Person person = authService.getPerson().get();
        model.addAttribute("person", person);
        // TODO добавить передачу заказанных книг
        model.addAttribute("likedBooks", peopleService.getLikedBooks(person.getId()));
        return "person/profile";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        Person person = authService.getPerson().get();
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String edit(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult) {

        Person personAuth = authService.getPerson().get();
        person.setId(personAuth.getId());

        //Проверяем все поля пользователя
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "person/edit";
        peopleService.update(personAuth.getId(), person);

        //Обновляем пользователя в сессии
        authService.getPersonDetails().update(person);
        return "person/profile";
    }
}
