package com.sdm.app.model.req.search;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchPin {
  private String role;
  private String dateSortBy;
  private String workUnit;
  @NotNull
  private Integer page;
  @NotNull
  private Integer size;

}
