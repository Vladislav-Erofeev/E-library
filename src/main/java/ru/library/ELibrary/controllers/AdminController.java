package ru.library.ELibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.PeopleService;
import ru.library.ELibrary.utils.BooksName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final BooksName booksName;

    private final String IMAGES_DIRECTORY;

    @Autowired
    public AdminController(PeopleService peopleService, BooksService booksService,
                           BooksName booksName, @Value("${upload.directory}") String upload_directory) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.booksName = booksName;
        IMAGES_DIRECTORY = upload_directory + "/images/books/";
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
        // TODO добавить передачу владельцев книг
        return "admin/showBook";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") int id,
                             Model model) throws IOException{
        Path fileNameAndPath = Paths.get( IMAGES_DIRECTORY, booksService.getById(id).getUrl());
        Files.delete(fileNameAndPath);
        booksService.delete(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/people")
    public String peoplePage(@RequestParam(value = "search", defaultValue = "", required = false)String name, Model model) {
        model.addAttribute("people", peopleService.findByName(name));
        return "admin/people";
    }

    @GetMapping("/people/{id}")
    public String personPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.getPerson(id));
        // TODO добавить передачу книг пользователя
        return "admin/showPerson";
    }

    @GetMapping("/add")
    public String addBookPage(@ModelAttribute("book")Book book) {
        return "admin/addBook";
    }

    @PostMapping("/add")
    public String saveBook(ModelMap model,
                                 @ModelAttribute("book") @Valid Book book,
                                 BindingResult bindingResult,
                           @RequestParam("image")MultipartFile file) throws IOException{
        if(bindingResult.hasErrors())
            return "admin/addBook";

        String fileName = booksName.name(file.getContentType());
        Path fileNameAndPath = Paths.get(IMAGES_DIRECTORY + "/images/books/", fileName);
        Files.write(fileNameAndPath, file.getBytes());
        book.setUrl(fileName);

        booksService.save(book);
        model.addAttribute("id", book.getId());
        return "redirect:/admin/books";
    }

    // TODO сделать систему оформления заказов
}
