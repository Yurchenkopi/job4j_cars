package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class  PostRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PostRepository.class.getName());

    private final CrudRepository crudRepository;

    public Optional<Post> create(Post post) {
        try {
            crudRepository.run(session -> session.save(post));
            return Optional.of(post);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean update(Post post) {
        return crudRepository.runAndReturnBool(session -> session.merge(post) != null);
    }


    public void delete(int postId) {
        crudRepository.run(
                "DELETE Post WHERE id = :fId",
                Map.of("fId", postId)
        );
    }

    public List<Post> findAll() {
        return crudRepository.query("""
    SELECT DISTINCT p
    FROM Post p
    JOIN FETCH p.prices
    JOIN FETCH p.participates
    JOIN FETCH p.car car
    JOIN FETCH car.engine
    JOIN FETCH car.owners
    ORDER BY p.id ASC
    """, Post.class);
    }

}
