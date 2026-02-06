package webb_kurs.gruppuppgift.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;


    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String role = "USER";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(

            name = "user_library",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<GameModel> games = new ArrayList<>();

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "USER";
    }
}

