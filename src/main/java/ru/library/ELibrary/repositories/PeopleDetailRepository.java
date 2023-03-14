package ru.library.ELibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.library.ELibrary.models.Person;

import java.util.Optional;

@Repository
public interface PeopleDetailRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByEmail(String email);
}
