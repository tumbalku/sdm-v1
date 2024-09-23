package com.sdm.app.model.req.create;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCutiRequest {

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

  private String signedBy;
}
