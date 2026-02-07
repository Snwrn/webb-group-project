package webb_kurs.gruppuppgift.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webb_kurs.gruppuppgift.DTO.CreateGameRequest;
import webb_kurs.gruppuppgift.model.GameModel;
import webb_kurs.gruppuppgift.service.GameService;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createGame(@RequestBody CreateGameRequest request) {
        try {
            var createdGame = gameService.createGame(request.title(), request.genre());
            return ResponseEntity.created(URI.create("/games/" + createdGame.getTitle())).body(createdGame);
        }
        catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{username}/{title}")
    public ResponseEntity<?> addGameToUser(@PathVariable String username, @PathVariable String title) {
        try {
            gameService.addGameToUser(username, title);
            return ResponseEntity.noContent().build();
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
        catch (AccessDeniedException e){
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameModel>> getAllGames() {
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findAllGamesByUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok(gameService.findAllGamesByUser(username));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(Map.of("error",e.getMessage()));
        }
    }

    @DeleteMapping("/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteGame(@PathVariable String title) {
        try {
            gameService.deleteGame(title);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping ("/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateGame(@PathVariable String title, @RequestBody GameModel updatedData) {
        try  {
            GameModel updatedGame = gameService.updateGame(title, updatedData);
            return ResponseEntity.ok(updatedGame);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
