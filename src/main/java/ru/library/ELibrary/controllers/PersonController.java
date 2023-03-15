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
import ru.library.ELibrary.services.PeopleService;
import ru.library.ELibrary.utils.PersonEditValidator;

@Controller
@RequestMapping("/profile")
public class PersonController {
    private final PeopleService peopleService;
    private final PersonEditValidator personValidator;

    @Autowired
    public PersonController(PeopleService peopleService, PersonEditValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
        model.addAttribute("person", person);
        // TODO добавить передачу заказанных книг
        model.addAttribute("likedBooks", peopleService.getLikedBooks(person.getId()));
        return "person/profile";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String edit(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person personAuth = personDetails.getPerson();
        person.setId(personAuth.getId());

        //Проверяем все поля пользователя
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "person/edit";
        peopleService.update(personAuth.getId(), person);

        //Обновляем пользователя в сессии
        personDetails.update(person);
        return "person/profile";
    }
}
