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
public class UserSearchRequest {
  private String identity;
  private String status;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;

}
