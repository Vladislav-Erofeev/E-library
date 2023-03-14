package ru.library.ELibrary.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
public class IndexController {
    @GetMapping("/index")
    public String indexPage(Model model) {
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
