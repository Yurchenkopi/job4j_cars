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
            var post1 = new Post();
            post1.setDescription("New Post1 Description");
            post1.setUser(user);
            post1.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
            post1.setParticipates(Set.of(user));
            var post2 = new Post();
            post2.setDescription("New Post2 Description");
            post2.setUser(user);
            post2.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
            post2.setParticipates(Set.of(user));
            var post3 = new Post();
            post3.setDescription("New Post3 Description");
            post3.setUser(user);
            post3.setPrices(List.of(
                    new PriceHistory(0, 555, 777),
                    new PriceHistory(0, 878, 333)
            ));
            post3.setParticipates(Set.of(user));
            var engineRepository = new EngineRepository(new CrudRepository(sf));
            var testEngine1 = new Engine();
            var testEngine2 = new Engine();
            var testEngine3 = new Engine();
            engineRepository.create(testEngine1);
            engineRepository.create(testEngine2);
            engineRepository.create(testEngine3);
            var ownerRepository = new OwnerRepository((new CrudRepository(sf)));
            var testOwner1 = new Owner();
            var testOwner2 = new Owner();
            var testOwner3 = new Owner();
            ownerRepository.create(testOwner1);
            ownerRepository.create(testOwner2);
            ownerRepository.create(testOwner3);
            var carRepository = new CarRepository(new CrudRepository(sf));
            var testCar1 = new Car();
            var testCar2 = new Car();
            var testCar3 = new Car();
            testCar1.setEngine(testEngine1);
            testCar1.setOwners(Set.of(testOwner1));
            testCar2.setEngine(testEngine2);
            testCar2.setOwners(Set.of(testOwner1, testOwner2));
            testCar3.setEngine(testEngine3);
            testCar3.setOwners(Set.of(testOwner1, testOwner2, testOwner3));
            carRepository.create(testCar1);
            carRepository.create(testCar2);
            carRepository.create(testCar3);
            post1.setCar(testCar1);
            postRepository.create(post1);
            post2.setCar(testCar2);
            postRepository.create(post2);
            post3.setCar(testCar3);
            postRepository.create(post3);
            System.out.println("--SELECT CURRENT DAY POSTS--");
            postRepository.findByCurrentDay()
                  .forEach(System.out::println);
            System.out.println("--SELECT POSTS BY ENGINE_ID--");
            System.out.println(postRepository.findByEngineId(testEngine1.getId()));
            System.out.println("--SELECT POSTS BY COUNT OF OWNERS--");
            postRepository.findByCountOfOwners(3L)
                     .forEach(System.out::println);
            System.out.println("-".repeat(100));
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