package com.sdm.app.model.req.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateKopRequest {

  @JsonIgnore
  private Long id;

  private String type;
  private String uniKop;

  private String romawi;

  private Integer year;
}
