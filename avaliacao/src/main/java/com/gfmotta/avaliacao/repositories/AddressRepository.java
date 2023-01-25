package com.gfmotta.avaliacao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gfmotta.avaliacao.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query(value = "SELECT a FROM Address a WHERE a.person.id = :personId")
	List<Address> findAllByPersonId(@Param("personId") Long personId);
	
	@Query(value = "SELECT a FROM Address a WHERE a.person.id = :personId AND a.mainAddress = true")
	Address findMainAddress(Long personId);
}
