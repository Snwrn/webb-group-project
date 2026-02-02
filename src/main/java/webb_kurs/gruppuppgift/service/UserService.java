package webb_kurs.gruppuppgift.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import webb_kurs.gruppuppgift.model.UserModel;
import webb_kurs.gruppuppgift.repository.IUserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel createUser(String username, String password) {
        if (username.isBlank() || username.length() < 4) {
            throw new IllegalArgumentException();
        }

        if (password.isBlank() || password.length() < 8) {
            throw new IllegalArgumentException("Password cannot be blank or shorter than 8 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit.");}



        if (IUserRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        var user = new UserModel(username, hashedPassword);
        user = IUserRepository.save(user);
        System.out.println("User '" + user.getId() + "' with name '" + user.getUsername() + "' created.");

        return user;
    } // TODO: Implement proper logging


    // TODO: fixa validering så admin enbart kan göra det
    public void deleteUser(String username) {

        UserModel user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        IUserRepository.delete(user);
    }

    public List<UserModel> getAllUsers() {
       return IUserRepository.findAll();
    }

    public UserModel getUserById(UUID id) {
        return IUserRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User with id" + id + " not found"));
    }
}
