package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Set;

public class HbmUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var userRepository = new UserRepositoryByCommandPattern(new CrudRepository(sf));
            var user = new User();
            user.setLogin("User3");
            user.setPassword("user3");
            userRepository.create(user);
            var postRepository = new PostRepository(new CrudRepository(sf));
            var post = new Post();
            post.setDescription("New Test Description");
            post.setUser(user);
            post.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
            post.setParticipates(Set.of(user));
            postRepository.create(post);
            postRepository.findAll()
                    .forEach(p -> System.out.println(p.getUser()));
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}