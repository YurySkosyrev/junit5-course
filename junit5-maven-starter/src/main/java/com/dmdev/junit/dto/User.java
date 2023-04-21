package com.dmdev.junit.dto;

import lombok.Value;

/**
 * Класс, описывающий сущность User для БД.
 *
 * @Value(staticConstructor="of"), делает конструктор со всеми аргументами приватным
 * и генерирует публичный статический метод с именем of,
 * который оборачивается вокруг приватного конструктора.
 */

@Value(staticConstructor = "of")
public class User {
    Integer id;
    String username;
    String password;
}
