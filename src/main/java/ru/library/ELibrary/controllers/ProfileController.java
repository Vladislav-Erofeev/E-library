package ru.library.ELibrary.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.security.PersonDetails;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
        model.addAttribute("person", person);
        // TODO добавить передачу заказанных книг
        // TODO добавить передачу понравившихся книг
        return "profile";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
        model.addAttribute("person", person);
        return "profile/edit";
    }
}
