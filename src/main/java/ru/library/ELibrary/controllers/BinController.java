package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;

@Controller
@RequestMapping("/bin")
public class BinController {
    private final AuthService authService;
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BinController(AuthService authService, PeopleService peopleService, BooksService booksService) {
        this.authService = authService;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping
    public String binPage(Model model) {
        Person person = authService.getPerson().get();
        model.addAttribute("books", peopleService.getPendingBooks(person.getId()));
        return "person/bin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFromBin(@PathVariable("id") int id) {
        Person person = authService.getPerson().get();
        booksService.deletePendingPerson(id, person);
        peopleService.deletePendingBook(person.getId(), booksService.getById(id));
        return "redirect:/bin";
    }
}
