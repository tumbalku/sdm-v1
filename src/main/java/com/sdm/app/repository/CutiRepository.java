package com.sdm.app.repository;

import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.User;
import com.sdm.app.model.res.CutiTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CutiRepository extends JpaRepository<Cuti, String>, JpaSpecificationExecutor<Cuti>  {

  List<Cuti> findByDateEndBefore(LocalDate date);
  List<Cuti> findByUser(User user);

  // rapihkan query ini
  @Query("SELECT new com.sdm.app.model.res.CutiTypeCount(c.kop.type, COUNT(c)) " +
          "FROM Cuti c " +
          "GROUP BY c.kop.type")
  List<CutiTypeCount> countByType();
}
