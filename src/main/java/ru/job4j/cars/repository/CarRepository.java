package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CarRepository {
    private static final Logger LOG = LoggerFactory.getLogger(CarRepository.class.getName());

    private final CrudRepository crudRepository;

    public Optional<Car> create(Car car) {
        try {
            crudRepository.run(session -> session.save(car));
            return Optional.of(car);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();

    }    public boolean update(Car car) {
        return crudRepository.runAndReturnBool(session -> session.merge(car) != null);
    }

    public void delete(int carId) {
        crudRepository.run(
                "DELETE Car WHERE id = :fId",
                Map.of("fId", carId)
        );
    }

    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "FROM Car WHERE id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    public Collection<Car> findAll() {
        return crudRepository.query("""
    SELECT DISTINCT c
    FROM Car c
    JOIN FETCH c.engine
    JOIN FETCH c.history_owners
    ORDER BY c.id ASC
    """, Car.class);
    }
}
