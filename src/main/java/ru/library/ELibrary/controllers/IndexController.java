package ru.library.ELibrary.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.security.PersonDetails;
import ru.library.ELibrary.services.AuthService;

import java.util.Optional;

@Controller
public class IndexController {
    private final AuthService authService;

    @Autowired
    public IndexController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }
    @GetMapping("/index")
    public String indexPage(Model model) {
        model.addAttribute("isAuthorised", false);
        Optional<Person> optionalPerson = authService.getPerson();
        if(optionalPerson.isPresent())
            model.addAttribute("isAuthorised", true);
        return "index";
    }

    @GetMapping("/errors")
    public String errorPage(HttpServletRequest request, Model model) {
        Object error = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer code = Integer.valueOf(error.toString());
        switch (code) {
            case 403:
                return "error/403";
            case 404:
                return "error/404";
        }
        return "index";
    }
}
