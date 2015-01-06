package kr.pe.kwonnam.replicationdatasource.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserOuterService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public User findByIdRead(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public User findByIdWrite(Integer id) {
        return userRepository.findById(id);
    }
}
