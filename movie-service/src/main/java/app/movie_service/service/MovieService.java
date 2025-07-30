package app.movie_service.service;

import app.movie_service.data.dto.request.MovieRequestDto;
import app.movie_service.data.dto.response.MovieResponseDto;
import app.movie_service.data.db.enums.Genre;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MovieService {

    MovieResponseDto addMovie(MovieRequestDto requestDto,String createdBy);

    Page<MovieResponseDto> getMovies(Genre genre, String name, String language, String sortBy, int pageNum, int pageSize);

    MovieResponseDto updateMovie(UUID id, MovieRequestDto requestDto, String updatedBy);

    String publishMovie(UUID id,String updatedBy);

    String unpublishMovie(UUID id,String updatedBy);
}
