package ru.library.ELibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.library.ELibrary.models.Book;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.BooksRepository;

import java.util.ArrayList;
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

    @Transactional
    public void save(Book book) {
        book.setViews(1);
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
        return booksRepository.findByNameContainingIgnoreCase(name,
                PageRequest.of(page, booksPerPage, Sort.by("views").descending()));
    }

    public Page<Book> getPage(String name, int page, int booksPerPage, Sort sort) {
        return booksRepository.findByNameContainingIgnoreCase(name,
                PageRequest.of(page, booksPerPage, sort.descending()));
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
        return booksRepository.findAll(PageRequest.of(0, 5,
                Sort.by("views").descending())).getContent();
    }

    private List<Book> generate100Books() {
        List<Book> books = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            String name = "Книга " + (i + 1);
            String author = "Автор для книги " + (i + 1);
            Book book = new Book();
            book.setName(name);
            book.setCount(12);
            book.setViews(1);
            book.setAuthor(author);
            book.setDescription("Lorem ipsum dolor sit amet, " +
                    "consectetuer adipiscing elit. Aenean commodo " +
                    "ligula eget dolor. Aenean massa. Cum sociis natoque " +
                    "penatibus et magnis dis parturient montes, nascetur " +
                    "ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu," +
                    " pretium quis, sem. Nulla consequat massa quis enim. Donec pede " +
                    "justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim " +
                    "justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis " +
                    "eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum " +
                    "semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor" +
                    " eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, " +
                    "viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius " +
                    "laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue.");
            book.setYear(2000);
            book.setUrl("hDQ792eanX2N3wwTI0c3.png");
            books.add(book);
        }
        return books;
    }

    @Transactional
    public void batchUpd() {
        booksRepository.saveAll(generate100Books());
    }
}
