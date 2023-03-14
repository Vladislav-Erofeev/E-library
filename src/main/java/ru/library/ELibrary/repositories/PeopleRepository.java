package ru.library.ELibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.library.ELibrary.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    List<Person> findByNameStartsWithIgnoreCase(String name);

    Optional<Person> findByEmail(String email);
}
