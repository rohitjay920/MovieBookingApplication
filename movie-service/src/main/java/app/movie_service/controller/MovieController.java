package app.movie_service.controller;

import app.movie_service.data.dto.request.MovieRequestDto;
import app.movie_service.data.dto.response.MovieResponseDto;
import app.movie_service.data.dto.ResponseStructure;
import app.movie_service.data.db.enums.Genre;
import app.movie_service.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<MovieResponseDto>> addMovie(@RequestBody MovieRequestDto requestDto,
                                                                           @RequestHeader String createdBy){
        MovieResponseDto response = movieService.addMovie(requestDto,createdBy);
        ResponseStructure<MovieResponseDto> apiResponse = ResponseStructure.<MovieResponseDto>genericBuilder().data(response)
                .statusCode(HttpStatus.CREATED.value()).message("Movie added successfully").build();
        return new ResponseEntity<ResponseStructure<MovieResponseDto>>(apiResponse,HttpStatus.CREATED);
    }

    @GetMapping("/getMovies")
    public ResponseEntity<ResponseStructure<Page<MovieResponseDto>>> getMovies(@RequestParam(required = false) Genre genre,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String language,
                                                             @RequestParam(required = false, defaultValue = "releaseDate")String sortBy,
                                                             @RequestParam(required = false, defaultValue = "0") int pageNum,
                                                             @RequestParam(required = false, defaultValue = "10") int pageSize){
        Page<MovieResponseDto> movies = movieService.getMovies(genre,name,language,sortBy,pageNum,pageSize);
        ResponseStructure<Page<MovieResponseDto>> apiResponse = ResponseStructure.<Page<MovieResponseDto>>genericBuilder().data(movies)
                .statusCode(HttpStatus.OK.value()).message("Movies found").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/updateMovie/{id}")
    public ResponseEntity<ResponseStructure<MovieResponseDto>> updateMovie(@PathVariable UUID id, @RequestBody MovieRequestDto requestDto, @RequestHeader String updatedBy){
        MovieResponseDto response = movieService.updateMovie(id,requestDto,updatedBy);
        ResponseStructure<MovieResponseDto> apiResponse = ResponseStructure.<MovieResponseDto>genericBuilder().data(response)
                .statusCode(HttpStatus.OK.value()).message("Movie updated").build();
        return ResponseEntity.ok(apiResponse);
    }

//    public ResponseEntity<String>

}
