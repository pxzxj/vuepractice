package io.github.pxzxj.user.controller;

import io.github.pxzxj.user.entity.User;
import io.github.pxzxj.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/page")
    public Page<User> page(Pageable pageable, String username) {
        return StringUtils.hasLength(username) ? userRepository.findByUsername(username, pageable) : userRepository.findAll(pageable);
    }

    @PostMapping("/delete")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping("/save")
    public String save(User user) {
        if(user.getId() == null){
            User existsUser = userRepository.findByUsername(user.getUsername());
            if(existsUser != null){
                return "用户名已经存在";
            }
        }
        userRepository.save(user);
        return "success";
    }

}
