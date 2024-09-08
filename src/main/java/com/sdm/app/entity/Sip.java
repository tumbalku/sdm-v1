package com.sdm.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sip")
public class Sip {

  @Id
  private String id;

  @Column(nullable = false, unique = true)
  private String num;
  private String name;
  private String path;
  private String fileType;
  private Integer size;
  private LocalDate expiredAt;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "sip", cascade = CascadeType.ALL)
  private List<SipReport> reports;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;
}
