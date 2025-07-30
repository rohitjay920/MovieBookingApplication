package app.movie_service.data.db.repository;

import app.movie_service.data.db.entities.Movie;
import app.movie_service.data.db.enums.Genre;
import org.springframework.data.jpa.domain.Specification;

public abstract class MovieSpecification {

    public static Specification<Movie> hasName(String name){

        return (root,query,cb) -> cb.like(cb.lower(root.get("name")),"%" +
                name.toLowerCase() + "%");
    }

    public static Specification<Movie> hasLanguage(String language){
        return (root,query,cb) -> cb.like(cb.lower(root.get("language")),
                "%" + language.toLowerCase() + "%");
    }

    public static Specification<Movie> hasGenre(Genre genre){
        return (root,query,cb) -> cb.equal(root.get("genre"),genre.name());
    }
}
