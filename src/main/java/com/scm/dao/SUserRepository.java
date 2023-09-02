package com.scm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.models.SUser;

public interface SUserRepository extends JpaRepository<SUser, Integer> {

	@Query("select u from SUser u where u.email= :email")
	public SUser getUserByUsername(@Param("email") String email);

}
