package com.sdm.app.entity;

import com.sdm.app.enumrated.KopType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Year;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "kops")
public class Kop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private KopType type;

  @Column(unique = true)
  private String uniKop;

  @Column(nullable = false)
  private String romawi;

  private Year year;

  @OneToMany(mappedBy = "kop", cascade = CascadeType.ALL)
  private Set<Cuti> cutis;
}
