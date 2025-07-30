package app.movie_service.data.db.entities;

import app.movie_service.data.db.enums.Genre;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@SQLDelete(sql = "update movie SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedMovieFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedMovieFilter", condition = "delete = :isDeleted")
public class Movie extends BaseClass{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String poster;
    private boolean deleted;

    @ElementCollection
    private List<String> screenTypes;

    @ElementCollection
    private List<String> languages;

    private ZonedDateTime releaseDate;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<Genre> genres;

    @OneToMany(mappedBy = "movie")
    private List<MovieCast> cast;
}
