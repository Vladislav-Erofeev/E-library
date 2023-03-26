package ru.library.ELibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.library.ELibrary.dto.OrderDTO;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.OrderStatus;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.AuthService;
import ru.library.ELibrary.services.BooksService;
import ru.library.ELibrary.services.OrderService;
import ru.library.ELibrary.services.PeopleService;
import ru.library.ELibrary.utils.PersonEditValidator;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class PersonController {
    private final PeopleService peopleService;
    private final OrderService orderService;
    private final BooksService booksService;
    private final PersonEditValidator personValidator;
    private final AuthService authService;

    @Autowired
    public PersonController(PeopleService peopleService, OrderService orderService, BooksService booksService, PersonEditValidator personValidator, AuthService authService) {
        this.peopleService = peopleService;
        this.orderService = orderService;
        this.booksService = booksService;
        this.personValidator = personValidator;
        this.authService = authService;
    }

    @GetMapping
    public String profilePage(Model model) {
        Person person = authService.getPerson().get();
        model.addAttribute("person", person);
        List<Order> orderedBooks = orderService.findOrderByPersonIdAndOrderStatus(person.getId(), OrderStatus.ORDERED);
        List<Order> takedBooks = orderService.findOrderByPersonIdAndOrderStatus(person.getId(), OrderStatus.TAKED);
        model.addAttribute("orderedBooks", orderedBooks.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        model.addAttribute("takedBooks", takedBooks.stream().map(this::ConvertToOrderDTO).collect(Collectors.toList()));
        return "person/profile";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        Person person = authService.getPerson().get();
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PatchMapping("/edit")
    public String edit(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult) {

        Person personAuth = authService.getPerson().get();
        person.setId(personAuth.getId());

        //Проверяем все поля пользователя
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors())
            return "person/edit";
        peopleService.update(personAuth, person);

        //Обновляем пользователя в сессии
        authService.getPersonDetails().update(person);
        return "redirect:/profile";
    }

    private OrderDTO ConvertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setData(order.getDate());
        orderDTO.setPerson(peopleService.getPerson(order.getPersonId()));
        orderDTO.setBook(booksService.getById(order.getBookId()));
        orderDTO.setOverdue(new Date(System.currentTimeMillis()).after(order.getDate()));
        return orderDTO;
    }
}
