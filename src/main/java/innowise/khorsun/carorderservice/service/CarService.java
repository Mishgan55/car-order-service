package innowise.khorsun.carorderservice.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CarService<T> {
    Optional<T> showOne(int id);
    List<T> findAll();
    void create(T car);
    void delete(int id);
    void update(Integer id,T carDto);

}
