package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getName());

    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public Optional<User> create(User user) {
        Session session = sf.openSession();
        Optional<User> rsl;
        try {
            session.beginTransaction();
            session.save(user);
            rsl = Optional.of(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.info("Объект user не был сохранен в БД по причине возникновения исключения.");
            rsl = Optional.empty();
        } finally {
            session.close();
        }
        return rsl;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET login = :fLogin, password = :fPassword WHERE id = :fId")
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :fId")
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        List<User> rsl;
        try {
            session.beginTransaction();
            rsl = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.info("Возникло исключение при поиске всех записей в БД.");
            rsl = Collections.emptyList();
        } finally {
            session.close();
        }
        return rsl;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Session session = sf.openSession();
        Optional<User> rsl;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User u where u.id = :fId", User.class);
            query.setParameter("fId", userId);
            rsl = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.info("Возникло исключение при поиске записи в БД по ID пользователя.");
            rsl = Optional.empty();
        } finally {
            session.close();
        }
        return rsl;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        List<User> rsl;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User u WHERE u.login LIKE :fKey", User.class);
            String modifyKey = String.format("%s%s%s", "%", key, "%");
            query.setParameter("fKey", modifyKey);
            rsl = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.info("Возникло исключение при поиске записи в БД по логину по маске.");
            rsl = Collections.emptyList();
        } finally {
            session.close();
        }
        return rsl;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Optional<User> rsl;
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User u where u.login = :fLogin", User.class);
            query.setParameter("fLogin", login);
            rsl = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.info("Возникло исключение при поиске записи в БД по логину.");
            rsl = Optional.empty();
        } finally {
            session.close();
        }
        return rsl;
    }
}
