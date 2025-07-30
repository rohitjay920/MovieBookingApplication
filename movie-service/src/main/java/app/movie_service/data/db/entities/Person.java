package app.movie_service.data.db.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Person extends BaseClass {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "person")
    private List<MovieCast> movieCasts;
}
