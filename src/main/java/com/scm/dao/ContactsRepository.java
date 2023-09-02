package com.scm.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.models.Contacts;

public interface ContactsRepository extends JpaRepository<Contacts, Integer> {

	@Query("from Contacts as c where c.suser.id =:userId")
	public Page<Contacts> findByUsername(@Param("userId") int userId, Pageable pageable);

	@Query("from Contacts as e where e.conEmail =:conEmail")
	public Contacts getContactByContactEmail(@Param("conEmail") String conEmail);

	// custom query method for search functionality
	@Query("SELECT c FROM Contacts c WHERE c.conName LIKE %?1%" + " OR c.conEmail LIKE %?1%"
			+ " OR c.conPhone LIKE %?1%")
	List<Contacts> searchByKeywordInContact(String keyword);

}
