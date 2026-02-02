package webb_kurs.gruppuppgift.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class GameModel {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @JsonIgnore
    @ManyToMany(mappedBy = "games")
    private List<UserModel> users = new ArrayList<>();

    public GameModel(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }
}
