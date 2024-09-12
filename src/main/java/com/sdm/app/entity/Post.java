package com.sdm.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

  @Id
  private String id;
  private String title;
  private String image;
  @Column(name = "content", columnDefinition="LONGTEXT")
  private String content;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int priority;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user", referencedColumnName = "id")
  private User user;

}
