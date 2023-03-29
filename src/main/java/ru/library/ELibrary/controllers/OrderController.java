package ru.library.ELibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.OrderStatus;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final BooksService booksService;
    private final AuthService authService;
    private final OrderService orderService;

    @Autowired
    public OrderController(BooksService booksService,
                           AuthService authService, OrderService orderService) {
        this.booksService = booksService;
        this.authService = authService;
        this.orderService = orderService;
    }

    @PostMapping("/add/{id}")
    public String addBook(@PathVariable("id")int id) {
        Person person = authService.getPerson().get();
        Book book = booksService.getById(id);

        Order order = new Order();
        order.setPersonId(person.getId());
        order.setBookId(book.getId());
        order.setOrderStatus(OrderStatus.ORDERED);
        orderService.save(order);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        Person person = authService.getPerson().get();
        Order order = orderService.findOrder(id, person.getId()).get();
        orderService.delete(order);
        return "redirect:/books/" + id;
    }
}
