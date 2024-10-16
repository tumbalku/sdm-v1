package com.sdm.app.repository;

import com.sdm.app.entity.Sip;
import com.sdm.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SipRepository extends JpaRepository<Sip, String>, JpaSpecificationExecutor<Sip> {
  List<Sip> findByUser(User user);

  @Query("SELECT COUNT(s) FROM Sip s WHERE s.expiredAt > CURRENT_DATE")
  long countActiveSips();
}
