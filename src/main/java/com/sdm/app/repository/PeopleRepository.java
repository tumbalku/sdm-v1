package com.sdm.app.repository;

import com.sdm.app.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long>, JpaSpecificationExecutor<People> {

  Optional<People> findByName(String name);
  Optional<People> findByNameIgnoreCase(String name);

}
