package io.github.pxzxj.user.repository;

import io.github.pxzxj.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);
}
