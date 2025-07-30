package app.movie_service.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseStructure<T> {
    private String message;
    private int statusCode;
    private T data;

    public static <T> ResponseStructureBuilder<T> genericBuilder(){
        return new ResponseStructureBuilder<T>();
    }
 }
