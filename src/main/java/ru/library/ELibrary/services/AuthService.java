package ru.library.ELibrary.services;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.security.PersonDetails;

import java.util.Optional;

@Service
public class AuthService {
    public Optional<Person> getPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Person> optionalPerson = Optional.empty();
        //Если пользователь не авторизован
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
            optionalPerson = Optional.of(personDetails.getPerson());
        }
        return optionalPerson;
    }

    public PersonDetails getPersonDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (PersonDetails) authentication.getPrincipal();
    }
}

