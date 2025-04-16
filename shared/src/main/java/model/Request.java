package model;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request<T> {
    private String action;
    private T data;
}
