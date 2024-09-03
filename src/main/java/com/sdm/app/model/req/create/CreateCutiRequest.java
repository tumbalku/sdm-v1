package com.sdm.app.model.req.create;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCutiRequest {

  private String id;
  private LocalDate dateStart;
  private LocalDate dateEnd;
  private Integer number;
  private List<String> people;
  private Long kop;
  private String user;
  private String address;

  private String signedBy;
}
