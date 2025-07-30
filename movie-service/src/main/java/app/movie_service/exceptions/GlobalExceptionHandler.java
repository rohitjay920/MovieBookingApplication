package app.movie_service.exceptions;

import app.movie_service.data.dto.ResponseStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementException(NoSuchElementException exception) {
        printLog(exception.getMessage());
//        logger.error("exception: {}",exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<Map<String, String>>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> error = exception.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage));
        printLog(error);
//        logger.error("errors: {}",error);
        ResponseStructure<Map<String, String>> response = ResponseStructure.<Map<String, String>>genericBuilder().data(error)
                .statusCode(HttpStatus.BAD_REQUEST.value()).message("method arguments not valid").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private static void printLog(Object error){
        log.error("exception: {}",error);
    }
}
