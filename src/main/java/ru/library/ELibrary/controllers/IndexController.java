package ru.library.ELibrary.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;

import java.util.Optional;

@Controller
public class IndexController {
    private final AuthService authService;
    private final BooksService booksService;

    @Autowired
    public IndexController(AuthService authService, BooksService booksService) {
        this.authService = authService;
        this.booksService = booksService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }
    @GetMapping("/index")
    public String indexPage(Model model) {
        model.addAttribute("isAuthorised", false);
        model.addAttribute("books", booksService.getTopBooks());
        Optional<Person> optionalPerson = authService.getPerson();
        if(optionalPerson.isPresent())
            model.addAttribute("isAuthorised", true);
        return "index";
    }

    @GetMapping("/errors")
    public String errorPage(HttpServletRequest request) {
        Object error = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int code = Integer.parseInt(error.toString());
        return switch (code) {
            case 403 -> "error/403";
            case 404 -> "error/404";
            default -> "index";
        };
    }
}
