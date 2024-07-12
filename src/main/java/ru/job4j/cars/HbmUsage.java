package ru.job4j.cars;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.EngineRepository;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

public class HbmUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var postRepository = new PostRepository(new CrudRepository(sf));
            var engineRepository = new EngineRepository(new CrudRepository(sf));
            System.out.println("--SELECT CURRENT DAY POSTS--");
            postRepository.findByCurrentDay()
                  .forEach(System.out::println);
            System.out.println("--SELECT POSTS BY ENGINE_ID--");
            System.out.println(postRepository.findByEngineId(1));
            System.out.println("--SELECT POSTS BY COUNT OF OWNERS--");
            postRepository.findByCountOfOwners(3L)
                     .forEach(System.out::println);
            System.out.println("--SELECT POSTS WITH PHOTO--");
            postRepository.findWithPhoto()
                    .forEach(System.out::println);
            System.out.println("--SELECT POSTS BY MODEL NAME--");
            postRepository.findByModelName("RaV")
                    .forEach(System.out::println);
            System.out.println("-".repeat(100));
            postRepository.findAllPostDto()
                    .forEach(System.out::println);
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
            engineRepository.findAll()
                    .forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}