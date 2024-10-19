package com.iffomko.voiceAssistant.db.services.dao;

import com.iffomko.voiceAssistant.db.entities.User;
import com.iffomko.voiceAssistant.db.repositories.UserRepository;
import com.iffomko.voiceAssistant.db.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация интерфейса <code>UserService</code>, которая умеет работать с пользователями (<code>User</code>)
 */
@Service("UserDAO")
@Transactional
public class UserDao implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserDao(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Добавляет нового пользователя в систему
     * @param user пользователь
     * @throws IllegalArgumentException если пользователь уже существует
     */
    @Override
    public void addUser(User user) throws IllegalArgumentException {
        if (repository.existsUserByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Такое имя пользователя уже существует");
        }

        repository.save(user);
    }

    /**
     * Удаляет существующего пользователя из системы по его id
     * @param id уникальный идентификатор пользователя
     * @return возвращает либо true в случае успешного удаления, либо false в случае неудачи
     */
    @Override
    public boolean deleteUserById(int id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }

    /**
     * Возвращает существующего пользователя по его id или возвращает null
     * @param id уникальный идентификатор пользователя
     * @return объект типа <code>User</code>
     */
    @Override
    public User getUserById(int id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Возвращает пользователя по его username или возвращает null
     * @param username username пользователя
     * @return объект типа <code>User</code>
     */
    @Override
    public User findUserByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    /**
     * Возвращает список всех пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
