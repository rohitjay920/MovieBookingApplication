package app.movie_service.data.dto.response;

import app.movie_service.data.db.enums.Genre;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MovieResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String poster;
    private List<String> screenTypes;
    private List<String> languages;
    private ZonedDateTime releaseDate;
    private List<Genre> genres;
    private boolean deleted;
}
