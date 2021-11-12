package com.webapplication.crossport.models.respository;


import com.webapplication.crossport.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer > {
}
