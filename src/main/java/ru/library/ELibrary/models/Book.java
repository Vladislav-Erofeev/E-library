package ru.library.ELibrary.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

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

    @Column(name = "count")
    private int count;

    @Column(name = "description")
    @NotEmpty(message = "Поле описание не должно быть пустым")
    private String description;
}
