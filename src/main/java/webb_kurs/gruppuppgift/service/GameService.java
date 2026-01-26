package webb_kurs.gruppuppgift.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import webb_kurs.gruppuppgift.model.GameModel;
import webb_kurs.gruppuppgift.model.UserModel;
import webb_kurs.gruppuppgift.repository.IGameRepository;
import webb_kurs.gruppuppgift.repository.IUserRepository;

@RequiredArgsConstructor
@Service
public class GameService {

    private final IGameRepository gameRepository;
    private final IUserRepository userRepository;

    public List<GameModel> findAllGames() {
        return gameRepository.findAll();
    }

    public GameModel createGame(String title, String genre) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title får inte vara tom");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre får inte vara tom");
        }

        if (gameRepository.findByTitle(title).isPresent()) {
            throw new RuntimeException("Game med title finns redan");
        }

        GameModel newGame = new GameModel(title, genre);
        return gameRepository.save(newGame);
    }

    public GameModel updateGame(String title, GameModel updatedData) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title får inte vara tom");
        }
        if (updatedData == null) {
            throw new IllegalArgumentException("updatedData får inte vara null");
        }

        GameModel existing = gameRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Game med title hittades inte"));

        existing.setTitle(updatedData.getTitle());
        existing.setGenre(updatedData.getGenre());

        return gameRepository.save(existing);
    }


    @Transactional
    public void addGameToUser(String username, String title) {

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User " + username + " not found"));

        GameModel game = gameRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Game" + title + " not found"));
        if
        (!user.getGames().contains(game)){
            user.getGames().add(game);
            userRepository.save(user);
        }
    }

    public List<GameModel> findAllGamesByUser(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User " + username + "not found"));

        return user.getGames();
    }

    public void deleteGame(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title får inte vara tom");
        }

        GameModel existing = gameRepository.findBytitle(title)
                .orElseThrow(() -> new RuntimeException("Game med title hittades inte"));

        gameRepository.delete(existing);
    }
}
