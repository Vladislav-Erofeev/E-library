package ru.library.ELibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

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
    @Min(value = 0, message = "Значение год должно быть больше 0")
    @NotEmpty(message = "Поле год не может быть пустым")
    private int year;

    @Column(name = "email")
    @Email(message = "Почта должна быть валидной")
    private String email;

    @Column(name = "phone")
    @NotEmpty(message = "Поле телефон не может быть пустым")
    private String phone;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;
}
