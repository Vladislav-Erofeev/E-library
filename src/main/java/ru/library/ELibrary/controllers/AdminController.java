package ru.library.ELibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/books")
    public String booksPage() {
        return "admin/books";
    }

    @GetMapping("/people")
    public String peoplePage() {
        return "admin/people";
    }
}
