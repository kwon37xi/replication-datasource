package kr.pe.kwonnam.replicationdatasource.controller;


import kr.pe.kwonnam.replicationdatasource.jpa.User;
import kr.pe.kwonnam.replicationdatasource.jpa.UserOuterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @Autowired
    private UserOuterService userOuterService;
    @GetMapping("test")
    public void saveWrite() throws Exception {
        User newUserFromRead = userOuterService.findByIdRead(1);
        User newUser = new User();
        newUser.setName("New User");

        userOuterService.save(newUser);
        System.out.println("***********************User saved : "+ newUser);

        newUserFromRead = userOuterService.findByIdRead(newUser.getId());
        System.out.println("***********************newUserFromRead : "+ (newUserFromRead==null));

        User newUserFromWrite = userOuterService.findByIdWrite(newUser.getId());
        System.out.println("***********************newUserFromRead : "+ (newUserFromWrite==null));
    }

}
