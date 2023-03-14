package ru.library.ELibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository repository;

    @Autowired
    public PeopleService(PeopleRepository repository) {
        this.repository = repository;
    }

    public List<Person> getPeopleList() {
        return repository.findAll();
    }

    public Person getPerson(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Person> findByName(String name) {
        return repository.findByNameStartsWithIgnoreCase(name);
    }

    public Optional<Person> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        repository.save(person);
    }
}
