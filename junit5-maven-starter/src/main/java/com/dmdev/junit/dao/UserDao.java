package com.dmdev.junit.dao;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Класс-заглушка моделирующий работу Dao-слоя.
 */
public class UserDao {

    @SneakyThrows
    public boolean delete(Integer userId) {
        try (Connection connection = DriverManager.getConnection("url", "username", "password")) {
            return true;
        }
    }
}
