package com.sdm.app.repository;


import com.sdm.app.entity.Letter;
import com.sdm.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LetterRepository extends JpaRepository<Letter, Long>, JpaSpecificationExecutor<Letter> {
  List<Letter> findByUser(User user);

}
