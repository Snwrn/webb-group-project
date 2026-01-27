package webb_kurs.gruppuppgift.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webb_kurs.gruppuppgift.model.GameModel;
import webb_kurs.gruppuppgift.model.UserModel;
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
    public ResponseEntity<?> createGame(@RequestBody CreateGameRequest request) {
        try {
            var createdGame = gameService.createGame(request.title(), request.genre());
            return ResponseEntity.created(URI.create("/games")).body(createdGame);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Failed to create a game."
                    ));
        }
    }

    public record CreateGameRequest(String title, String genre) {

    }

    @GetMapping("/all")
    public ResponseEntity<List<GameModel>> getAllGames() {
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteGame(@PathVariable String title) {
        gameService.deleteGame(title);
        return ResponseEntity.noContent().build();
    }

}
