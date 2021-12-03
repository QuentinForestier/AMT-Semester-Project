package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Integer> {
}