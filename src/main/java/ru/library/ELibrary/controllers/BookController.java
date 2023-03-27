package ru.library.ELibrary.controllers;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.OrderService;
import ru.library.ELibrary.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final OrderService orderService;

    private final AuthService authService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService,
                          OrderService orderService, AuthService authService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping
    private String booksPage(@RequestParam(value = "search", defaultValue = "") String name,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "books_per_page", defaultValue = "2")int booksPerPage,
                             Model model) {
        Optional<Person> optionalPerson = authService.getPerson();
        model.addAttribute("isAuthorised", false);
        if(optionalPerson.isPresent())
            model.addAttribute("isAuthorised", true);

        model.addAttribute("books", booksService.getPage(name, page, booksPerPage).get());
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("search", name);
        model.addAttribute("pageCount", booksService.getPage(name, page, booksPerPage).getTotalPages());

        return "book/books";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable("id")int id, Model model) {
        Book book = booksService.getById(id);
        model.addAttribute("book", book);
        booksService.incrViews(id);
        Optional<Person> optionalPerson = authService.getPerson();
        model.addAttribute("isAuthorised", false);
        model.addAttribute("isLiked", null);
        model.addAttribute("isOrdered", false);
        model.addAttribute("isFree", true);
        if(orderService.getByBookId(id).size() == book.getCount()) {
            model.addAttribute("isFree", false);
        }

        if(optionalPerson.isPresent()) {
            model.addAttribute("isAuthorised", true);
            Person person = optionalPerson.get();
            model.addAttribute("isLiked", booksService.isLiked(person.getId(), id));
            Optional<Order> optionalOrder = orderService.findOrder(id, person.getId());
            if(optionalOrder.isPresent())
                model.addAttribute("isOrdered", true);
        }

        return "book/showBook";
    }

    @PostMapping("/{id}")
    public String likeBook(@PathVariable("id")int id) {
        Person person = authService.getPerson().get();
        booksService.addLikedPerson(id, peopleService.getPerson(id));
        peopleService.addLikedBook(person.getId(), booksService.getById(id));
        return "redirect:/books/"+id;
    }

    @DeleteMapping("/{id}")
    public String deleteLikeBook(@PathVariable("id")int id) {
        Person person = authService.getPerson().get();
        booksService.deleteLikedPerson(id, peopleService.getPerson(id));
        peopleService.deleteLikedBook(person.getId(), booksService.getById(id));
        return "redirect:/books/"+id;
    }
}
