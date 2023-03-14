package ru.library.ELibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.PeopleRepository;

@Service
public class PeopleRegistrationService {
    private final PeopleRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public PeopleRegistrationService(PeopleRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public void registrate(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        repository.save(person);
    }
}
