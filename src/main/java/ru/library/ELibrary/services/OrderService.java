package ru.library.ELibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.OrderStatus;
import ru.library.ELibrary.repositories.OrderRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void save(Order order) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        order.setDate(calendar.getTime());
        orderRepository.save(order);
    }

    public Optional<Order> findOrder(int bookId, int personId) {
        return orderRepository.findByBookIdAndAndPersonId(bookId, personId);
    }

    @Transactional
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> findOrderByPersonIdAndOrderStatus(int personId, OrderStatus orderStatus) {
        return orderRepository.findOrderByPersonIdAndOrderStatus(personId, orderStatus);
    }

    public Order getById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void takeBook(Order order) {
        order.setOrderStatus(OrderStatus.TAKED);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        order.setDate(calendar.getTime());
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Transactional
    public void updateDate(Order order) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        order.setDate(calendar.getTime());
        orderRepository.save(order);
    }

    public List<Order> findByBookIdAnsStatus(int bookId, OrderStatus orderStatus) {
        return orderRepository.findByBookIdAndOrderStatus(bookId, orderStatus);
    }

    public List<Order> getByBookId(int bookId) {
        return orderRepository.findByBookId(bookId);
    }
}
