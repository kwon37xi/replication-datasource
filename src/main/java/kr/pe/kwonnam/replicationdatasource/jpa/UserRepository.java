package kr.pe.kwonnam.replicationdatasource.jpa;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepository {
    @javax.persistence.PersistenceContext
    private EntityManager entityManager;


    public User findById(Integer id) {
        return entityManager.find(User.class, id);
    }

    public void save(User user) {
        entityManager.persist(user);
    }
}
