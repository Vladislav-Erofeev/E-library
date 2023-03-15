package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.security.PersonDetails;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    private String booksPage(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "books_per_page", defaultValue = "0")int booksPerPage,
                             Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = new Person();
        model.addAttribute("isAuthorised", false);

        //Если пользователь не авторизован
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuthorised", true);
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            person = personDetails.getPerson();
        }
        model.addAttribute("person", person);
        model.addAttribute("books", booksService.getPage(page, booksPerPage).get());
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("pageCount", booksService.getPage(page, booksPerPage).getTotalPages());
        return "book/books";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable("id")int id, Model model) {
        model.addAttribute("book", booksService.getById(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("isAuthorised", false);
        //Если пользователь не авторизован
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuthorised", true);
            PersonDetails personDetails = (PersonDetails)authentication.getPrincipal();
            Person person = personDetails.getPerson();
            model.addAttribute("isLiked", booksService.isLiked(person.getId(), id));
            return "book/showBook";
        }
        model.addAttribute("isLiked", null);
        return "book/showBook";
    }

    @PostMapping("/{id}")
    public String likeBook(@PathVariable("id")int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person person = new Person();
        model.addAttribute("isAuthorised", false);

        //Если пользователь не авторизован
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuthorised", true);
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            person = personDetails.getPerson();
        }
        model.addAttribute("person", person);

        booksService.addLikedPerson(id, peopleService.getPerson(id));
        peopleService.addLikedBook(person.getId(), booksService.getById(id));

        model.addAttribute("book", booksService.getById(id));
        model.addAttribute("isLiked", person);

        return "book/showBook";
    }

}
