package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.utils.CrudRepository;

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
            user.setLogin("log_User1");
            user.setPassword("pass_user1");
            userRepository.create(user);
            var postRepository = new PostRepository(new CrudRepository(sf));
            postRepository.delete(24);
            var post = new Post();
            post.setDescription("New User1 Description");
            post.setUser(user);
            post.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
           post.setParticipates(Set.of(user));
           var engineRepository = new EngineRepository(new CrudRepository(sf));
           var testEngine = new Engine();
           engineRepository.create(testEngine);
           var ownerRepository = new OwnerRepository((new CrudRepository(sf)));
           var testOwner1 = new Owner();
           var testOwner2 = new Owner();
           ownerRepository.create(testOwner1);
           ownerRepository.create(testOwner2);
           var carRepository = new CarRepository(new CrudRepository(sf));
           var testCar = new Car();
           testCar.setEngine(testEngine);
           testCar.setOwners(Set.of(testOwner1, testOwner2));
           carRepository.create(testCar);
           post.setCar(testCar);
           postRepository.create(post);
           postRepository.findAll()
                   .forEach(System.out::println);
           postRepository.findAll().stream()
                   .map(Post::getUser)
                   .forEach(System.out::println);
           postRepository.findAll().stream()
                   .map(Post::getCar)
                   .forEach(System.out::println);
           postRepository.findAll().stream()
                   .map(Post::getCar)
                   .map(Car::getOwners)
                   .forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}