package ru.library.ELibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull(message = "Поле имя не может быть пустым")
    private String name;

    @Column(name = "gender")
    @NotNull(message = "Поле пол не может быть пустым")
    private String gender;

    @Column(name = "year")
    @Min(value = 0, message = "Значение год должно быть больше 0")
    @NotNull(message = "Поле год не может быть пустым")
    private int year;

    @Column(name = "email")
    @Email
    @NotNull(message = "Поле почта не может быть пустым")
    private String email;

    @Column(name = "phone")
    @NotNull
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;
}
