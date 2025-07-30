package app.movie_service.service.serviceImplementation;

import app.movie_service.data.dto.request.MovieRequestDto;
import app.movie_service.data.dto.response.MovieResponseDto;
import app.movie_service.data.db.entities.Movie;
import app.movie_service.data.db.enums.Genre;
import app.movie_service.data.db.repository.MovieRepository;
import app.movie_service.data.db.repository.MovieSpecification;
import app.movie_service.service.MovieService;
import app.movie_service.utils.CommonConstants;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImplementation implements MovieService {

    private final ModelMapper mapper;
    private final MovieRepository movieRepository;
    private final EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(MovieServiceImplementation.class);

    // need to accept email instead of createdBy so that we can validate the user through db and then set it or else , can be done
    // through jwt
    @Override
    public MovieResponseDto addMovie(MovieRequestDto requestDto,String createdBy) {
        Movie movie = mapper.map(requestDto,Movie.class);
        movie.setCreatedBy(createdBy);
        movie.setUpdatedBy(null);

        Movie saved = movieRepository.save(movie);
        MovieResponseDto response = mapper.map(saved,MovieResponseDto.class);
        logger.info("MovieResponse: {}",response);
        return response;
    }

    // need to make enhancements in getMovies api to be dynamically accessible for every filter

    @Override
    public Page<MovieResponseDto> getMovies(Genre genre, String name, String language, String sortBy, int pageNum, int pageSize) {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedMovieFilter").setParameter("isDeleted",false);

        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);

        Specification<Movie> spec = (root,query,cb) -> cb.conjunction();

        if(name!=null && !name.isBlank()){
            spec = spec.and(MovieSpecification.hasName(name));
        }
        if(language!=null && !language.isBlank()){
            spec = spec.and(MovieSpecification.hasLanguage(language));
        }
        if(genre!=null){
            spec = spec.and(MovieSpecification.hasGenre(genre));
        }

        Page<Movie> page = movieRepository.findAll(spec,pageable);
        logger.info("Movies: {}", page);
        return page.map(movie -> mapper.map(movie,MovieResponseDto.class));
    }

    @Override
    public MovieResponseDto updateMovie(UUID id, MovieRequestDto requestDto, String updatedBy) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Movie with ID: "+id+" not found"));

        movie.setUpdatedBy(updatedBy);
        movie.setGenres(requestDto.getGenres());
        movie.setName(requestDto.getName());
        movie.setPoster(requestDto.getPoster());
        movie.setDescription(requestDto.getDescription());
        movie.setLanguages(requestDto.getLanguages());
        movie.setScreenTypes(requestDto.getScreenTypes());
        movie.setReleaseDate(requestDto.getReleaseDate());

        Movie updated = movieRepository.save(movie);
        logger.info("movie updated: {}",updated);
        return mapper.map(updated,MovieResponseDto.class);
    }

    @Override
    public String publishMovie(UUID id,String updatedBy) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Movie with ID: "+id+" not found"));
        movie.setDeleted(false);
        movie.setUpdatedBy(updatedBy);
        movieRepository.save(movie);
        return CommonConstants.MOVIE_PUBLISHED;
    }

    @Override
    public String unpublishMovie(UUID id, String updatedBy) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Movie with ID: "+id+" not found"));
        movie.setDeleted(true);
        movie.setUpdatedBy(updatedBy);
        movieRepository.save(movie);
        return CommonConstants.MOVIE_UNPUBLISHED;
    }
}
