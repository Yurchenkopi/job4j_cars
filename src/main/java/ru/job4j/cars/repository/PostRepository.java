package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PostRepository {

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

    public void delete(int postId) {
        crudRepository.run(
                "DELETE Post WHERE id = :fId",
                Map.of("fId", postId)
        );
    }

    public List<Post> findAll() {
        return crudRepository.query("FROM Post ORDER BY id ASC", Post.class);
    }

}
