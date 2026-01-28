package webb_kurs.gruppuppgift.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webb_kurs.gruppuppgift.model.UserModel;
import webb_kurs.gruppuppgift.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            var createdUser = userService.createUser(request.username(), request.password());
            return ResponseEntity.created(URI.create("/users")).body(createdUser);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Failed to create a user."
                    ));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
    //DTO flytta till egen mapp?
    public record CreateUserRequest(String username, String password) { }
}

