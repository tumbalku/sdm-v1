package com.sdm.app.repository;

import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.CutiStatus;
import com.sdm.app.model.res.CutiTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CutiRepository extends JpaRepository<Cuti, String>, JpaSpecificationExecutor<Cuti> {

  List<Cuti> findByDateEndBefore(LocalDate date);

  List<Cuti> findByUser(User user);

  //  @Query("SELECT new com.sdm.app.model.res.CutiTypeCount(c.kop.type, COUNT(c)) " +
//          "FROM Cuti c " +
//          "GROUP BY c.kop.type")
  @Query("SELECT new com.sdm.app.model.res.CutiTypeCount(c.kop.type, COUNT(c)) " +
          "FROM Cuti c " +
          "WHERE c.status IN :statuses " +
          "GROUP BY c.kop.type")
  List<CutiTypeCount> countByType(@Param("statuses") List<CutiStatus> statuses);

  @Query("SELECT new com.sdm.app.model.res.CutiTypeCount(c.kop.type, COUNT(c)) " +
          "FROM Cuti c " +
          "WHERE c.status = :status " +
          "GROUP BY c.kop.type")
  List<CutiTypeCount> countByType(@Param("status") CutiStatus status);

  @Query("SELECT SUM(COUNT(c)) FROM Cuti c " +
          "WHERE c.status = :status " +
          "GROUP BY c.kop.type")
  long countByStatus(@Param("status") CutiStatus status);

  @Query("SELECT new com.sdm.app.model.res.CutiTypeCount(c.kop.type, COUNT(c)) " +
          "FROM Cuti c " +
          "GROUP BY c.kop.type")
  List<CutiTypeCount> countByAllTypes();

  List<Cuti> findByUserAndDateEndAfter(User user, LocalDate currentDate);

  List<Cuti> findByUserAndStatus(User user, CutiStatus status);

  List<Cuti> findByUserAndDateEndAfterAndStatus(User user, LocalDate currentDate, CutiStatus status);

  List<Cuti> findByUserAndDateEndAfterAndStatusNot(User user, LocalDate currentDate, CutiStatus status);

  @Query("SELECT c FROM Cuti c WHERE YEAR(c.createdAt) = :year AND c.status = :status")
  List<Cuti> findByCreatedAtYearAndStatus(@Param("year") int year, @Param("status") CutiStatus status);
}
