package com.sdm.app.model.req.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdm.app.enumrated.CutiStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecitionCutiRequest {

  @JsonIgnore
  private String id;
  private String number;
  private String message;
  private CutiStatus status;
  private List<String> people;
  private String signedBy;
}
