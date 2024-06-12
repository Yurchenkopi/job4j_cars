package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.util.List;

public class HbmUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var userRepository = new UserRepositoryByCommandPattern(new CrudRepository(sf));
            var user = new User();
            user.setLogin("User");
            user.setPassword("user");
            userRepository.create(user);
            var postRepository = new PostRepository(new CrudRepository(sf));
            var post = new Post();
            post.setDescription("New Test Description");
            post.setUser(user);
            post.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
            postRepository.create(post);
            postRepository.findAll()
                    .forEach(System.out::println);
            postRepository.delete(post.getId());
            userRepository.delete(user.getId());
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}