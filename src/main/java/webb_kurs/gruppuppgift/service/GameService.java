package webb_kurs.gruppuppgift.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

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
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty.");
        }

        if (gameRepository.findByTitle(title).isPresent()) {
            throw new IllegalArgumentException("Game with title already exist");
        }

        GameModel newGame = new GameModel(title, genre);
        return gameRepository.save(newGame);
    }

    public GameModel updateGame(String title, GameModel updatedData) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (updatedData == null) {
            throw new IllegalArgumentException("updatedData cannot be null.");
        }

        GameModel existing = gameRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Game with title wasn't found"));

        existing.setTitle(updatedData.getTitle());
        existing.setGenre(updatedData.getGenre());

        return gameRepository.save(existing);
    }


    @Transactional
    public void addGameToUser(String username, String title) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("You need to log in to do this.");
        }
        String loggedInUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (!loggedInUsername.equals(username)) {
            throw new AccessDeniedException("You can only add games to your own account");
        }

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        GameModel game = gameRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        user.getGames().add(game);
        userRepository.save(user);
    }

    public List<GameModel> findAllGamesByUser(String username) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("You need to log in to do this.");
        }
        String loggedInUsername = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (!loggedInUsername.equals(username)) {
            throw new AccessDeniedException("You can only see your own games.");
        }
        
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User " + username + "not found"));

        return user.getGames();
    }

    public void deleteGame(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        GameModel existing = gameRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Game with title wasn't found!"));


        for (UserModel user : existing.getUsers()) {
            user.getGames().remove(existing);
        }

        gameRepository.delete(existing);
    }
}
