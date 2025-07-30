package app.movie_service.data.dto.request;

import app.movie_service.data.db.enums.Genre;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MovieRequestDto {
    private String name;
    private String description;
    private String poster;
    private List<String> screenTypes;
    private List<String> languages;
    private ZonedDateTime releaseDate;
    private List<Genre> genres;
}
