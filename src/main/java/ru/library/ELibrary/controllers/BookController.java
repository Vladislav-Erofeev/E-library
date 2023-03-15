package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.library.ELibrary.services.BooksService;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;

    @Autowired
    public BookController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    private String booksPage(@RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "books_per_page", defaultValue = "0")int booksPerPage,
                             Model model) {
        model.addAttribute("books", booksService.getPage(page, booksPerPage).get());
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("pageCount", booksService.getPage(page, booksPerPage).getTotalPages());
        return "book/books";
    }


    //TODO сделать возможность лайкать книгу
}
