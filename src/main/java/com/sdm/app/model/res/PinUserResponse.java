package com.sdm.app.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PinUserResponse {

  private String id;
  private String name;
  private String workUnit;
  private String position;
  private String avatar;
}
