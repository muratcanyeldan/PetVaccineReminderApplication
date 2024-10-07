package org.muratcan.petvaccine.reminder.service;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.entity.User;
import org.muratcan.petvaccine.reminder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }

    public boolean saveChatIdForUser(String email, String chatId) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setChatId(chatId);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
