package ru.library.ELibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.library.ELibrary.models.Person;
import ru.library.ELibrary.repositories.PeopleDetailRepository;
import ru.library.ELibrary.security.PersonDetails;

import java.util.Optional;

@Service
public class PeopleDetailService implements UserDetailsService {
    private final PeopleDetailRepository repository;

    @Autowired
    public PeopleDetailService(PeopleDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = repository.findByEmail(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("user not found");
        return new PersonDetails(person.get());
    }
}
