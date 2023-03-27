package ru.library.ELibrary.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Fetch;

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

    @Column(name = "last_name")
    @NotEmpty(message = "Поле фамилия не может быть пустым")
    private String lastName;

    @Column(name = "surname")
    @NotEmpty(message = "Поле отчество не может быть пустым")
    private String surname;


    @Column(name = "year")
    @Min(value = 1, message = "Значение год должно быть больше 0")
    private int year;

    @Column(name = "email")
    @Email(message = "Почта должна быть валидной")
    @NotEmpty(message = "Поле почта не должно быть пустым")
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    @NotEmpty(message = "Поле пароль не может быть пустым")
    private String password;

    @ManyToMany
    @JoinTable(name = "liked_book",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> likedBooks;

    @ManyToMany
    @JoinTable(name = "order_book",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> orderedBooks;

    public void addLikedBook(Book book) {
        if(likedBooks == null)
            likedBooks = new ArrayList<>();
        likedBooks.add(book);
    }
}
