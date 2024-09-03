package com.sdm.app.repository;

import com.sdm.app.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  Optional<Address> findByNameIgnoreCase(String name);

  boolean existsByNameIgnoreCase(String name);
}
