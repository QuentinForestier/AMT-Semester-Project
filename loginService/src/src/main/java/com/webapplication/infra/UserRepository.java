package com.webapplication.infra;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    User findFirstByUsername(@Param("username") String username);
}