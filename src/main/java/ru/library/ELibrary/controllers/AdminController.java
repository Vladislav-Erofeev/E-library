package ru.library.ELibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.library.ELibrary.dto.OrderDTO;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.OrderStatus;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.OrderService;
import ru.library.ELibrary.services.PeopleService;
import ru.library.ELibrary.utils.BooksName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PeopleService peopleService;
    private final BooksService booksService;
    private final OrderService orderService;
    private final BooksName booksName;

    private final String IMAGES_DIRECTORY;

    @Autowired
    public AdminController(PeopleService peopleService, BooksService booksService,
                           OrderService orderService, BooksName booksName, @Value("${upload.directory}") String upload_directory) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.orderService = orderService;
        this.booksName = booksName;
        IMAGES_DIRECTORY = upload_directory + "/books/";
    }

    @GetMapping("/books")
    public String booksPage(@RequestParam(value = "search", defaultValue = "", required = false) String name,
                            @RequestParam(value = "page", defaultValue = "0")int page,
                            Model model) {
        Page page1 =  booksService.getPage(name, page, 40, Sort.by("name"));
        model.addAttribute("books", page1);
        model.addAttribute("search", name);
        model.addAttribute("pageCount", page1.getTotalPages());
        return "admin/books";
    }

    @GetMapping("/books/{id}")
    public String bookPage(@PathVariable("id") int id,
                           Model model) {
        model.addAttribute("book", booksService.getById(id));
        List<Order> ordered = orderService.findByBookIdAnsStatus(id, OrderStatus.ORDERED);
        List<Order> taked = orderService.findByBookIdAnsStatus(id, OrderStatus.TAKED);
        model.addAttribute("ordered", ordered.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        model.addAttribute("taked", taked.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        return "admin/showBook";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) throws IOException{
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
        List<Order> orderedBooks = orderService.findOrderByPersonIdAndOrderStatus(id, OrderStatus.ORDERED);
        List<Order> takedBooks = orderService.findOrderByPersonIdAndOrderStatus(id, OrderStatus.TAKED);
        model.addAttribute("orderedBooks", orderedBooks.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        model.addAttribute("takedBooks", takedBooks.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        return "admin/showPerson";
    }

    @PostMapping("/order")
    public String takeBook(@RequestParam("order")int id) {
        Order order = orderService.getById(id);
        orderService.takeBook(order);
        return "redirect:/admin/people/" + order.getPersonId();
    }

    @PatchMapping("/order")
    public String changeOrderDate(@RequestParam("order") int id) {
        Order order = orderService.getById(id);
        orderService.updateDate(order);
        return "redirect:/admin/people/" + order.getPersonId();
    }

    @DeleteMapping("/order")
    public String deleteBookfromOrder(@RequestParam("order")int id) {
        Order order = orderService.getById(id);
        orderService.deleteOrder(order);
        return "redirect:/admin/people/" + order.getPersonId();
    }

    @PostMapping("/books/order")
    public String take(@RequestParam("order")int id) {
        Order order = orderService.getById(id);
        orderService.takeBook(order);
        return "redirect:/admin/books/" + order.getBookId();
    }

    @PatchMapping("/books/order")
    public String changeDate(@RequestParam("order") int id) {
        Order order = orderService.getById(id);
        orderService.updateDate(order);
        return "redirect:/admin/books/" + order.getBookId();
    }

    @DeleteMapping("/books/order")
    public String delete(@RequestParam("order")int id) {
        Order order = orderService.getById(id);
        orderService.deleteOrder(order);
        return "redirect:/admin/books/" + order.getBookId();
    }

    @GetMapping("/add")
    public String addBookPage(@ModelAttribute("book")Book book) {
        return "admin/addBook";
    }

    @GetMapping("/batch")
    public String batchUpd() {
        booksService.batchUpd();
        return "redirect:/admin/books";
    }

    @PostMapping("/add")
    public String saveBook(ModelMap model,
                                 @ModelAttribute("book") @Valid Book book,
                                 BindingResult bindingResult,
                           @RequestParam("image")MultipartFile file) throws IOException{
        if(bindingResult.hasErrors())
            return "admin/addBook";

        String fileName = booksName.name(file.getContentType());
        Path fileNameAndPath = Paths.get(IMAGES_DIRECTORY, fileName);
        Files.write(fileNameAndPath, file.getBytes());
        book.setUrl(fileName);

        booksService.save(book);
        model.addAttribute("id", book.getId());
        return "redirect:/admin/books";
    }

    private OrderDTO ConvertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setData(order.getDate());
        orderDTO.setId(order.getId());
        orderDTO.setPerson(peopleService.getPerson(order.getPersonId()));
        orderDTO.setBook(booksService.getById(order.getBookId()));
        orderDTO.setOverdue(new Date(System.currentTimeMillis()).after(order.getDate()));
        return orderDTO;
    }
}
