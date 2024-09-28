package com.sdm.app.entity;

import com.sdm.app.enumrated.CutiStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cutis")
public class Cuti {

  @Id
  private String id;
  private String number;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  @Column(nullable = false)
  private String romawi;
  private String address;
  private String workUnit;
  @Column(columnDefinition="LONGTEXT")
  private String message;
  @Enumerated(value = EnumType.STRING)
  private CutiStatus status;
  @Column(columnDefinition="LONGTEXT")
  private String reason;
  private Year year;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "cuti_people",
          joinColumns = @JoinColumn(name = "cuti_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "people_id", referencedColumnName = "id")
  )
  private List<People> people = new ArrayList<>();

  private String signedBy;
  private String mark;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "kop_id", referencedColumnName = "id",  nullable = false)
  private Kop kop;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;
}
