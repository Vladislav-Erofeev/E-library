package ru.library.ELibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.library.ELibrary.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
