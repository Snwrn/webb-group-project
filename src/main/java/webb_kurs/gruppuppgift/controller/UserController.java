package webb_kurs.gruppuppgift.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webb_kurs.gruppuppgift.model.UserModel;
import webb_kurs.gruppuppgift.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserModel user) {
        try {
            var createdUser = userService.createUser(user.getUsername(), user.getPassword());
            return ResponseEntity.created(URI.create("/users")).body(createdUser);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Failed to create a user."
                    ));
        }
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}