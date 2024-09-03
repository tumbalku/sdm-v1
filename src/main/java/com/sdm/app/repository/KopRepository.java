package com.sdm.app.repository;

import com.sdm.app.entity.Kop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KopRepository extends JpaRepository<Kop, Long> {
}
