package com.iffomko.voiceAssistant.db.services;

import com.iffomko.voiceAssistant.db.entities.User;

import java.util.List;

/**
 * Интерфейс, который фиксирует контракт по работе с таблицей в базе данных с пользователями
 */
public interface UserService {
    /**
     * Добавляет нового пользователя в систему
     * @param user пользователь
     */
    void addUser(User user);

    /**
     * Удаляет существующего пользователя из системы по его id
     * @param id уникальный идентификатор пользователя
     * @return возвращает либо true в случае успешного удаления, либо false в случае неудачи
     */
    boolean deleteUserById(int id);

    /**
     * Возвращает существующего пользователя по его id
     * @param id уникальный идентификатор пользователя
     * @return объект типа <code>User</code>
     */
    User getUserById(int id);

    /**
     * Возвращает пользователя по его username
     * @param username username пользователя
     * @return объект типа <code>User</code>
     */
    User findUserByUsername(String username);

    /**
     * Возвращает список всех пользователей
     */
    List<User> getAllUsers();
}
