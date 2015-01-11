package kr.pe.kwonnam.replicationdatasource.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A service for a nested transaction test
 */
@Service
public class UserInnerService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User findByUserIdWithPropagationRequired(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public User findByUserIdWithPropagationRequiresNew(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public User findByUserIdWithPropagationMandatory(Integer id) {
        return userRepository.findById(id);
    }
}
