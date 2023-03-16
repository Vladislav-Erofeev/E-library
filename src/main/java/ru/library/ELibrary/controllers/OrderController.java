package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final AuthService authService;

    @Autowired
    public OrderController(PeopleService peopleService, BooksService booksService, AuthService authService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.authService = authService;
    }

    @PostMapping("/add/{id}")
    public String addToBin(@PathVariable("id")int id) {
        Person person = authService.getPerson().get();
        booksService.addPendingPerson(id, peopleService.getPerson(person.getId()));
        peopleService.addPendingBook(person.getId(), booksService.getById(id));
        return "redirect:/books/" + id;
    }
}
