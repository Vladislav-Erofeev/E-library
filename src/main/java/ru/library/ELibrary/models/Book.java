package ru.library.ELibrary.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Поле имя не может быть пустым")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Поле автор не должно быть пустым")
    private String author;

    @Column(name = "year")
    @Min(value = 1, message = "Значение поля год должно быть больше 0")
    private int year;

    @Column(name = "count")
    @Min(value = 1, message = "Значение поля количество должно быть больше 0")
    private int count;

    @Column(name = "description")
    @NotEmpty(message = "Поле описание не должно быть пустым")
    private String description;
}
