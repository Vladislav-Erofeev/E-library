package ru.library.ELibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getBooks() {
        return booksRepository.findAll();
    }

    public Book getById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    public List<Book> findByName(String name) {
        return booksRepository.findByNameStartsWithIgnoreCase(name);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Page<Book> getPage(int page, int booksPerPage) {
        if(booksPerPage == 0)
            return new PageImpl<>(booksRepository.findAll());
        return booksRepository.findAll(PageRequest.of(page, booksPerPage));
    }

    @Transactional
    public void addLikedPerson(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        Hibernate.initialize(book.getLikedPerson());
        book.addLikedPerson(person);
    }

    public Person isLiked(int person_id, int book_id) {
        Book book = booksRepository.findById(book_id).get();
        return book.getLikedPerson().stream().filter(person -> person.getId() == person_id).findAny().orElse(null);
    }

    @Transactional
    public void deleteLikedPerson(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        book.getLikedPerson().remove(person);
    }

    @Transactional
    public void deletePendingPerson(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        book.getPendingPerson().remove(person);
    }

    @Transactional
    public void addPendingPerson(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        Hibernate.initialize(book.getPendingPerson());
        book.addPendingPerson(person);
    }
}
