package kr.pe.kwonnam.lazyreplicationdatasourceproxy.jpa;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public User findById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public void save(User user) {
        entityManager.persist(user);
    }
}
