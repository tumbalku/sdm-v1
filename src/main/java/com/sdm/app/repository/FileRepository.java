package com.sdm.app.repository;

import com.sdm.app.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
  Optional<File> findByPath(String path);

  void deleteByPath(String path);
}
