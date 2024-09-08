package com.sdm.app.model.res;

import com.sdm.app.enumrated.LetterType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SipResponse {

  private String id;
  private String name;
  private String num;
  private String path;
  private String fileType;
  private Integer size;
  private LocalDate expiredAt;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;
  private SimpleUserResponse user;

  private List<SipReportResponse> reports;
}
