package ru.library.ELibrary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Person;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private Person person;
    private Book book;
    private Date data;
    private boolean isOverdue;
}
