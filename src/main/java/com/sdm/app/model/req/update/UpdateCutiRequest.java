package com.sdm.app.model.req.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.app.enumrated.CutiStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCutiRequest {

  @JsonIgnore
  private String id;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  private String number;
  private List<String> people;
  private Long kop;
  private String user;
  private String address;
  private String mark;
  private String message;
  private CutiStatus status;
  private String workUnit;

  private String signedBy;
}
