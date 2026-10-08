package io.github.pxzxj.user.repository;

import io.github.pxzxj.user.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
}
