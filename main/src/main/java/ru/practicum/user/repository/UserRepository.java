package ru.practicum.user.repository;

import ru.practicum.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Поиск пользователя по имени почте.
    User findByName(String userName);

    // Поиск пользователя по электронной почте.
    User findByEmail(String userEmail);
}