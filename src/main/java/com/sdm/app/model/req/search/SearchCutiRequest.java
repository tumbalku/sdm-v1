package com.sdm.app.model.req.search;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCutiRequest {

  private String name;
  private String type;

  private List<String> statuses;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
