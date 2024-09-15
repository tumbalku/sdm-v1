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
public class UserSearchRequest {
  private String identity;
  private String status;
  private List<String> roles;
  @NotNull
  private Integer page;
  @NotNull
  private Integer size;

}
