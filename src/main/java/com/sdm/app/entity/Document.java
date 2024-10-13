package com.sdm.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {
  @Id
  @Column(name = "id", nullable = false)
  private String id;
  private String name;
  private String path;
  @Column(nullable = false, columnDefinition = "int default 0")
  private int priority;
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;
  private String type;
  private Integer size;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;


}