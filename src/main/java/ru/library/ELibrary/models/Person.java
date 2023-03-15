package ru.library.ELibrary.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Поле имя не может быть пустым")
    private String name;

    @Column(name = "gender")
    @NotEmpty(message = "Поле пол не может быть пустым")
    private String gender;

    @Column(name = "year")
    @Min(value = 1, message = "Значение год должно быть больше 0")
    private int year;

    @Column(name = "email")
    @Email(message = "Почта должна быть валидной")
    @NotEmpty(message = "Поле почта не должно быть пустым")
    private String email;

    @Column(name = "phone")
    @NotEmpty(message = "Поле телефон не может быть пустым")
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    @NotEmpty(message = "Поле пароль не может быть пустым")
    private String password;

    @ManyToMany
    @JoinTable(name = "liked_books",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> likedBooks;

    public void addLikedBook(Book book) {
        if(likedBooks == null)
            likedBooks = new ArrayList<>();
        likedBooks.add(book);
    }
}
