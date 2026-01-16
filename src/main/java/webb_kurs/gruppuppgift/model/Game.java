package webb_kurs.gruppuppgift.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Entity(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class Game {

    @Id
    private final UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Game(String title) {
        this.title = title;
    }

}
