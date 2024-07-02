package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EngineRepository.class.getName());

    private final CrudRepository crudRepository;

    public Optional<Engine> create(Engine engine) {
        try {
            crudRepository.run(session -> session.save(engine));
            return Optional.of(engine);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean update(Engine engine) {
        return crudRepository.runAndReturnBool(session -> session.merge(engine) != null);
    }

    public void delete(int engineId) {
        crudRepository.run(
                "DELETE Engine WHERE id = :fId",
                Map.of("fId", engineId)
        );
    }

    public Optional<Engine> findById(int engineId) {
        return crudRepository.optional(
                "FROM Engine WHERE id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }

    public List<Engine> findAll() {
        return crudRepository.query("""
    FROM Engine e
    ORDER BY e.id ASC
    """, Engine.class);
    }

}
