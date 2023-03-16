package ru.library.ELibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Book;
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
    public void update(Person oldPerson, Person newPerson) {
        newPerson.setId(oldPerson.getId());
        newPerson.setLikedBooks(oldPerson.getLikedBooks());
        newPerson.setPassword(oldPerson.getPassword());
        newPerson.setRole(oldPerson.getRole());
        repository.save(newPerson);
    }

    @Transactional
    public void addLikedBook(int id, Book book) {
        Person person = repository.findById(id).get();
        Hibernate.initialize(person.getLikedBooks());
        person.addLikedBook(book);
    }

    public List<Book> getLikedBooks(int id) {
        return repository.findById(id).get().getLikedBooks();
    }

    @Transactional
    public void deleteLikedBook(int id, Book book) {
        Person person = repository.findById(id).get();
        person.getLikedBooks().remove(book);
    }

    public List<Book> getPendingBooks(int id) {
        return repository.findById(id).get().getPendingBooks();
    }

    @Transactional
    public void addPendingBook(int id, Book book) {
        Person person = repository.findById(id).get();
        Hibernate.initialize(person.getPendingBooks());
        person.addPendingBook(book);
    }

    @Transactional
    public void deletePendingBook(int id, Book book) {
        Person person = repository.findById(id).get();
        person.getPendingBooks().remove(book);
    }
}
