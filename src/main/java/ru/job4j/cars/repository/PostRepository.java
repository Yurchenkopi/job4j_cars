package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.dto.PostDto;
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
        return crudRepository.query("""
    SELECT DISTINCT p
    FROM Post p
    JOIN FETCH p.prices
    JOIN FETCH p.participates
    JOIN FETCH p.car car
    JOIN FETCH p.files
    JOIN FETCH car.engine
    JOIN FETCH car.color
    JOIN FETCH car.model m
    JOIN FETCH car.owners
    JOIN FETCH car.histories
    LEFT JOIN FETCH car.regNumbers
    JOIN FETCH m.manufacturer
    JOIN FETCH m.bodyType
    JOIN FETCH m.category
    ORDER BY p.id ASC
    """, Post.class);
    }

    public Collection<PostDto> findAllPostDto() {
        return crudRepository.query("""
    SELECT new ru.job4j.cars.dto.PostDto(p.id, p.description)
    FROM Post p
    ORDER BY p.id ASC
    """, PostDto.class);
    }

    /**
     *Show current day posts
     */
    public Collection<Post> findByCurrentDay() {
        return crudRepository.query(
             """
            SELECT DISTINCT p
            FROM Post p
            JOIN FETCH p.prices pr
            JOIN FETCH p.participates
            JOIN FETCH p.car car
            JOIN FETCH p.files
            JOIN FETCH car.engine
            JOIN FETCH car.color
            JOIN FETCH car.model m
            JOIN FETCH car.owners
            JOIN FETCH car.histories
            LEFT JOIN FETCH car.regNumbers
            JOIN FETCH m.manufacturer
            JOIN FETCH m.bodyType
            JOIN FETCH m.category
            WHERE pr.created BETWEEN :fStartDateTime AND :fEndDateTime
            ORDER BY p.id ASC
            """,
                Post.class,
                Map.of(
                        "fStartDateTime", LocalDateTime.now().minusDays(1),
                        "fEndDateTime", LocalDateTime.now()
                )
        );
    }

    /**
     *Show post with required engine_id
     */
    public Optional<Post> findByEngineId(int engineId) {
        return crudRepository.optional(
                """
               FROM Post p
               JOIN FETCH p.prices pr
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               JOIN FETCH p.files
               JOIN FETCH car.engine eng
               JOIN FETCH car.color
               JOIN FETCH car.model m
               JOIN FETCH car.owners
               JOIN FETCH car.histories
               LEFT JOIN FETCH car.regNumbers
               JOIN FETCH m.manufacturer
               JOIN FETCH m.bodyType
               JOIN FETCH m.category
               WHERE eng.id = :fEngineId
               """,
                Post.class,
                Map.of(
                        "fEngineId", engineId
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
               JOIN FETCH p.prices pr
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               JOIN FETCH p.files
               JOIN FETCH car.engine
               JOIN FETCH car.owners own
               JOIN FETCH car.color
               JOIN FETCH car.model m
               JOIN FETCH car.histories
               LEFT JOIN FETCH car.regNumbers
               JOIN FETCH m.manufacturer
               JOIN FETCH m.bodyType
               JOIN FETCH m.category
               WHERE lower(m.modelName) LIKE lower(:fName)
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
               JOIN FETCH p.prices pr
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               LEFT JOIN FETCH p.files photos
               JOIN FETCH car.engine
               JOIN FETCH car.owners own
               JOIN FETCH car.color
               JOIN FETCH car.model m
               JOIN FETCH car.histories
               LEFT JOIN FETCH car.regNumbers
               JOIN FETCH m.manufacturer
               JOIN FETCH m.bodyType
               JOIN FETCH m.category
               WHERE photos.id IS NOT NULL
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
               JOIN FETCH p.prices pr
               JOIN FETCH p.participates
               JOIN FETCH p.car car
               JOIN FETCH p.files
               JOIN FETCH car.engine
               JOIN FETCH car.owners own
               JOIN FETCH car.color
               JOIN FETCH car.model m
               JOIN FETCH car.histories
               LEFT JOIN FETCH car.regNumbers
               JOIN FETCH m.manufacturer
               JOIN FETCH m.bodyType
               JOIN FETCH m.category
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
