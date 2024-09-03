package com.sdm.app.model.res;

import com.sdm.app.enumrated.LetterType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterResponse {

  private Long id;
  private String name;
  private String num;
  private String path;
  private String fileType;
  private Integer size;
  @Enumerated(value = EnumType.STRING)
  private LetterType type;
  private LocalDate expiredAt;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;
  private SimpleUserResponse user;
}
