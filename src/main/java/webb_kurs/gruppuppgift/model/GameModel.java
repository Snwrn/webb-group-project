package webb_kurs.gruppuppgift.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "games")
public class GameModel {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(unique = true, nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String genre;

    @Setter
    @ManyToMany(mappedBy = "games")
    private List<UserModel> users = new ArrayList<>();

    public GameModel() {
    }

    public GameModel(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public List<UserModel> getUsers() {
        return users;
    }
}