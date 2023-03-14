package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public AdminController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping("/books")
    public String booksPage(@RequestParam(value = "search", defaultValue = "", required = false) String name,
                            Model model) {
        model.addAttribute("books", booksService.findByName(name));
        return "admin/books";
    }

    @GetMapping("/books/{id}")
    public String bookPage(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("book", booksService.getById(id));
        return "admin/showBook";
    }

    @GetMapping("/people")
    public String peoplePage(@RequestParam(value = "search", defaultValue = "", required = false)String name, Model model) {
        model.addAttribute("people", peopleService.findByName(name));
        return "admin/people";
    }

    @GetMapping("/people/{id}")
    public String personPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getPerson(id));
        return "admin/showPerson";
    }
}
