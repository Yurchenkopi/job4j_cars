package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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

    public Collection<Post> findAll() {
        return crudRepository.query(
                """
    SELECT DISTINCT p
    FROM Post p
    JOIN FETCH p.prices
    JOIN FETCH p.participates
    LEFT JOIN FETCH p.files
    ORDER BY p.id ASC
    """, Post.class);
    }

    /**
     *Show current day posts
     */
    public Collection<Post> findByCurrentDay() {
        return crudRepository.query(
            """
            SELECT DISTINCT p
            FROM Post p
            JOIN FETCH p.prices
            JOIN FETCH p.participates
            LEFT JOIN FETCH p.files
            WHERE p.created BETWEEN :fStartDateTime AND current_timestamp
            ORDER BY p.id ASC
            """,
                Post.class,
                Map.of(
                        "fStartDateTime", LocalDateTime.now().minusDays(1)
                )
        );
    }

    /**
     *Show posts with required modelName
     */
    public Collection<Post> findByModelName(String modelName) {
        return crudRepository.query(
                """
               SELECT DISTINCT p
               FROM Post p
               JOIN FETCH p.prices
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               JOIN FETCH car.model m
               JOIN FETCH p.files
               WHERE lower(m.modelName) LIKE lower(:fName)
               ORDER BY p.id ASC
               """,
                Post.class,
                Map.of(
                        "fName", String.format("%s%s%s", "%", modelName, "%")
                )
        );
    }

    /**
     *Show posts with photo
     */
    public Collection<Post> findWithPhoto() {
        return crudRepository.query(
               """
               SELECT DISTINCT p
               FROM Post p
               JOIN FETCH p.prices
               JOIN FETCH p.participates
               LEFT JOIN FETCH p.files photos
               WHERE SIZE (photos) <> 0
               ORDER BY p.id ASC
               """,
                Post.class,
                Collections.emptyMap()
        );
    }

    /**
     *Show posts with required count of owners
     */
    public Collection<Post> findByCountOfOwners(Long count) {
        return crudRepository.query(
                """
               SELECT DISTINCT p
               FROM Post p
               JOIN FETCH p.prices
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               JOIN FETCH p.files
               JOIN FETCH car.owners own
               WHERE car.id IN (
                    SELECT car.id
                    FROM Car car
                    JOIN car.owners own
                    GROUP BY car.id
                    HAVING count(own.id) >= :fCount
                    )
               """,
                Post.class,
                Map.of(
                        "fCount", count
                )
        );
    }

}
