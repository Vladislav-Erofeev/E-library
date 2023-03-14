package ru.library.ELibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Book;
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
}
