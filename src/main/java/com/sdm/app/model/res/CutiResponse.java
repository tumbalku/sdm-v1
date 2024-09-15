package com.sdm.app.model.res;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutiResponse {

  private String id;
  private String address;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  private String number;
  private String reason;
  private String message;
  private String status;
  private List<String> people;
  private KopResponse kop;
  private SimpleUserResponse user;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String signedBy;
}
