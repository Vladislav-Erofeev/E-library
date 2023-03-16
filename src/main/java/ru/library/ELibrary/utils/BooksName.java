package ru.library.ELibrary.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;


@Component
public class BooksName {
    public String name(String type) {
        String name  = RandomStringUtils.random(20, true, true);
        name += "." + type.substring(6);
        return name.toString();
    }
}
