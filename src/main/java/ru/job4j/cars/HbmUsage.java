package ru.job4j.cars;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.PostRepository;
import ru.job4j.cars.repository.utils.CrudRepository;

import java.util.Collection;

public class HbmUsage {
    public static void printPostInfo(Post post) {
            var p = new Post();
            p.setId(post.getId());
            p.setCreated(post.getCreated());
            p.setDescription(post.getDescription());
            p.setPrices(post.getPrices());
            p.setFiles(post.getFiles());
            p.setStatus(post.isStatus());
            p.setUser(post.getUser());
            System.out.println(p);
    }

    public static void printPostInfo(Collection<Post> posts) {
        posts.forEach(HbmUsage::printPostInfo);
    }

    public static void printCarInfo(Car car) {
        var model = new Model();
        model.setModelName(car.getModel().getModelName());
        model.setManufacturer(car.getModel().getManufacturer());
        var c = new Car();
        c.setEngine(car.getEngine());
        c.setColor(car.getColor());
        c.setModel(model);
        System.out.println(c);
    }

    public static void printCarInfo(Collection<Car> cars) {
        cars.forEach(HbmUsage::printCarInfo);
    }

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            var postRepository = new PostRepository(new CrudRepository(sf));
            var carRepository = new CarRepository(new CrudRepository(sf));
            System.out.println("--SELECT CURRENT DAY POSTS--");
            printPostInfo(postRepository.findByCurrentDay());
            System.out.println("--SELECT POSTS WITH PHOTO--");
            printPostInfo(postRepository.findWithPhoto());
            System.out.println("--SELECT POSTS BY MODEL NAME--");
            printPostInfo(postRepository.findByModelName("RaV"));
            System.out.println("--SELECT CARS BY ENGINE_ID--");
            printCarInfo((carRepository.findByEngineId(1)).get());
            System.out.println("--SELECT POSTS BY COUNT OF OWNERS--");
            printPostInfo(postRepository.findByCountOfOwners(3L));
            System.out.println("-".repeat(100));
            printPostInfo(postRepository.findAll());

        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}