package ru.library.ELibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.BooksRepository;

import java.util.List;

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

    /**
     * Поиск и получение страницы с результатом
     * @param name - строка для поиска
     * @param page - номер страницы
     * @param booksPerPage - количество элементов
     * @return Страницу с найденными элементами
     */
    public Page<Book> getPage(String name, int page, int booksPerPage) {
        return booksRepository.findByNameStartsWithIgnoreCase(name,
                PageRequest.of(page, booksPerPage, Sort.by("views").descending()));
    }

    /**
     * Добавление человека при лайке
     * @param id - id книги в бд
     * @param person - человек, который лайкнул книгу
     */
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

    @Transactional
    public void addUrl(int id, String url) {
        Book book = booksRepository.findById(id).get();
        book.setUrl(url);
    }

    @Transactional
    public void incrViews(int id) {
        Book book = booksRepository.findById(id).get();
        if(book.getViews() == null)
            book.setViews(0);
        book.setViews(book.getViews() + 1);
    }

    public List<Book> getTopBooks() {
        List<Book> books= booksRepository.findAll(PageRequest.of(0, 5,
                Sort.by("views").descending())).getContent();
        return books;
    }
}
