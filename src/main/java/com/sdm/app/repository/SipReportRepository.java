package com.sdm.app.repository;

import com.sdm.app.entity.SipReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SipReportRepository extends JpaRepository<SipReport, Long> {
}
