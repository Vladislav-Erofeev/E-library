package ru.library.ELibrary.repositories;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.library.ELibrary.models.Order;
import ru.library.ELibrary.models.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByBookIdAndAndPersonId(int bookId, int personId);
    List<Order> findOrderByPersonIdAndOrderStatus(int personId, OrderStatus orderStatus);
    List<Order> findByBookIdAndOrderStatus(int bookId, OrderStatus orderStatus);
    List<Order> findByBookId(int bookId);
}
