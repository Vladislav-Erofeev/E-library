package ru.library.ELibrary.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@Table(name = "order_book")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "person_id")
    private int personId;

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
