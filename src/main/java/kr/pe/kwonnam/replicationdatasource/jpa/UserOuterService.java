package kr.pe.kwonnam.replicationdatasource.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserOuterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInnerService userInnerService;

    /**
     * readOnly = true에 따라 Read DB에서 데이터를 읽는다.
     *
     */
    @Transactional(readOnly = true)
    public User findByIdRead(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * readOnly = false에 따라 Write DB에서 데이터를 읽는다.
     *
     */
    @Transactional(readOnly = false)
    public User findByIdWrite(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public void save(User user) throws Exception {
        userRepository.save(user);
    }

    /**
     * 비록 {@link UserInnerService#findByUserIdWithPropagationRequired(Integer)} 가 readOnly = true라 하여도
     * Propagation.REQUIRED는 기존 트랜잭션이 존재할 경우 기존 트랜잭션과 커넥션을 재사용하기 때문에 바깥 트랜잭션을 따라
     * Write DB로 요청을 보낸다.
     * <p/>
     * 물론 기존 트랜잭션이 존재하지 않을 경우에는 새로운 트랜잭션을 맺기 때문에 올바르게 작동한다.
     */
    @Transactional(readOnly = false)
    public Map<String, User> findByIdWriteAndInnerReadWithPropagationRequired(Integer outerFirstId, Integer innerId, Integer outerSecondId) {
        Map<String, User> users = new HashMap<String, User>();
        users.put("outerFirstUser", userRepository.findById(outerFirstId));
        users.put("innerUser", userInnerService.findByUserIdWithPropagationRequired(innerId));
        users.put("outerSecondUser", userRepository.findById(outerSecondId));
        return users;
    }

    /**
     * {@link UserInnerService#findByUserIdWithPropagationRequiresNew(Integer)}가 readOnly = true이고,
     * Propagation.REQUIRES_NEW이면 기존 트랜잭션이 아닌 새로운 트랜잭션을 생성하며 새로운 커넥션을 맺는다.
     * 따라서 innerUser를 read DB에서 읽어오게 된다.
     */
    @Transactional(readOnly = false)
    public Map<String, User> findByIdWriteAndInnerReadWithPropagationRequiresNew(Integer outerFirstId, Integer innerId, Integer outerSecondId) {
        Map<String, User> users = new HashMap<String, User>();
        users.put("outerFirstUser", userRepository.findById(outerFirstId));
        users.put("innerUser", userInnerService.findByUserIdWithPropagationRequiresNew(innerId));
        users.put("outerSecondUser", userRepository.findById(outerSecondId));
        return users;
    }

    /**
     * {@link UserOuterService#findByIdWriteAndInnerReadWithPoropagationMandatory(Integer, Integer, Integer)}는
     * 항상 바깥 트랜잭션을 따르기 때문에 바깥 트랜잭션과 커넥션을 재사용한다. 따라서 readOnly=true라도 Write DB에서 값을 읽어온다.
     */
    @Transactional(readOnly = false)
    public Map<String, User> findByIdWriteAndInnerReadWithPoropagationMandatory(Integer outerFirstId, Integer innerId, Integer outerSecondId) {
        Map<String, User> users = new HashMap<String, User>();
        users.put("outerFirstUser", userRepository.findById(outerFirstId));
        users.put("innerUser", userInnerService.findByUserIdWithPropagationMandatory(innerId));
        users.put("outerSecondUser", userRepository.findById(outerSecondId));
        return users;
    }
}
