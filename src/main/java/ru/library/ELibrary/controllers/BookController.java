package ru.library.ELibrary.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    private final AuthService authService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService, AuthService authService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.authService = authService;
    }

    @GetMapping
    private String booksPage(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "books_per_page", defaultValue = "0")int booksPerPage,
                             Model model) {
        Optional<Person> optionalPerson = authService.getPerson();
        model.addAttribute("isAuthorised", false);
        if(optionalPerson.isPresent())
            model.addAttribute("isAuthorised", true);

        model.addAttribute("books", booksService.getPage(page, booksPerPage).get());
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("pageCount", booksService.getPage(page, booksPerPage).getTotalPages());

        return "book/books";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable("id")int id, Model model) {
        model.addAttribute("book", booksService.getById(id));
        Optional<Person> optionalPerson = authService.getPerson();
        model.addAttribute("isAuthorised", false);
        model.addAttribute("isLiked", null);

        if(optionalPerson.isPresent()) {
            model.addAttribute("isAuthorised", true);
            Person person = optionalPerson.get();
            model.addAttribute("isLiked", booksService.isLiked(person.getId(), id));
        }

        return "book/showBook";
    }

    @PostMapping("/{id}")
    public String likeBook(@PathVariable("id")int id, Model model) {
        Person person = authService.getPerson().get();
        booksService.addLikedPerson(id, peopleService.getPerson(id));
        peopleService.addLikedBook(person.getId(), booksService.getById(id));
        return "redirect:/books/"+id;
    }

    @DeleteMapping("/{id}")
    public String deleteLikeBook(@PathVariable("id")int id, Model model) {
        Person person = authService.getPerson().get();
        booksService.deleteLikedPerson(id, peopleService.getPerson(id));
        peopleService.deleteLikedBook(person.getId(), booksService.getById(id));
        return "redirect:/books/"+id;
    }
}
