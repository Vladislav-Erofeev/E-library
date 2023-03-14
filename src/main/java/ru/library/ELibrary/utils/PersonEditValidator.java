package ru.library.ELibrary.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.services.PeopleService;

@Component
public class PersonEditValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonEditValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleService.findByEmail(person.getEmail()).isPresent() &&
                person.getId() != peopleService.findByEmail(person.getEmail()).get().getId()) {
            errors.rejectValue("email", "", "Пользователь с таким email уже существует");
        }
    }
}
