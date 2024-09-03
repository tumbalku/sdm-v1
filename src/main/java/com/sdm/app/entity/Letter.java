package com.sdm.app.entity;

import com.sdm.app.enumrated.LetterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "letters")
public class Letter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String num;
  private String name;
  private String path;
  private String fileType;
  private Integer size;
  @Enumerated(value = EnumType.STRING)
  private LetterType type;
  private LocalDate expiredAt;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;
}
