package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    /**
     * поправь инкапсуляцию в классе UserDaoImpl
     */
    @PersistenceContext()
    private EntityManager entityManager;

    @Override
    public List<User> getUser() {
        return entityManager.createQuery(
                "from User", User.class).getResultList();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    /**
     * в методе поиска, обновления и удаления проверяй, что запись с таким id существует,
     * если нет выбрасывай соответствующее исключение с сообщением об ошибке -
     * EntityNotFoundException (либо своё кастомное)
     */

    @Override
    public User findByIdUser(long id) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id = :userId", User.class);
        query.setParameter("userId", id);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Не найден пользователь с Id = " + id);
        } else if (users.size() > 1) {
            throw new IllegalStateException("Найдено более одного пользователя с Id = " + id);
        }
        return users.get(0);
    }


    @Override
    public void removeUser(long id) {
        User user = findByIdUser(id);
        entityManager.remove(user);
    }

    @Override
    public void updateUser(User user) {
        User user1 = findByIdUser(user.getId());
        user1.setName(user.getName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setAge(user.getAge());
    }
}
