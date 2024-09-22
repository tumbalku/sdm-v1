package com.sdm.app.model.req.search;

import com.sdm.app.enumrated.CutiStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCutiRequest {

  private String name;
  private String type;

  private String status;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
