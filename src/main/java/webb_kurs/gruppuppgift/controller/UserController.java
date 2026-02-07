package webb_kurs.gruppuppgift.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import webb_kurs.gruppuppgift.DTO.CreateUserRequest;
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
            return ResponseEntity.created(URI.create("/users/" + createdUser.getUsername())).body(createdUser);
        } catch ( IllegalArgumentException | IllegalStateException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", exception.getMessage()));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try{
            return ResponseEntity.ok(userService.getUserById(userId));
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));        }
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error,", e.getMessage()));
        }
    }

}

