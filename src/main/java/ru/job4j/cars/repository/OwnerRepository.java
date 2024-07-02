package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class OwnerRepository {
    private static final Logger LOG = LoggerFactory.getLogger(OwnerRepository.class.getName());

    private final CrudRepository crudRepository;

    public Optional<Owner> create(Owner owner) {
        try {
            crudRepository.run(session -> session.save(owner));
            return Optional.of(owner);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean update(Owner owner) {
        return crudRepository.runAndReturnBool(session -> session.merge(owner) != null);
    }

    public void delete(int ownerId) {
        crudRepository.run(
                "DELETE Owner WHERE id = :fId",
                Map.of("fId", ownerId)
        );
    }

    public Optional<Owner> findById(int ownerId) {
        return crudRepository.optional(
                "FROM Owner WHERE id = :fId", Owner.class,
                Map.of("fId", ownerId)
        );
    }

    public List<Owner> findAll() {
        return crudRepository.query("""
    FROM Owner o
    ORDER BY o.id ASC
    """, Owner.class);
    }
}
