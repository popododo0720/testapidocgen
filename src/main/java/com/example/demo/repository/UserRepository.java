package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    public UserRepository() {
        // 초기 데이터
        save(new User(1L, "alice", "alice@example.com", 25, null));
        save(new User(2L, "bob", "bob@example.com", 30, null));
        save(new User(3L, "charlie", "charlie@example.com", 35, null));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
            user.setCreatedAt(LocalDateTime.now());
        }
        store.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<User> findByUsername(String username) {
        return store.values().stream()
                .filter(user -> user.getUsername().contains(username))
                .toList();
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }
}