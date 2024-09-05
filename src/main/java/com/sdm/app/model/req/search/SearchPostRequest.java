package com.sdm.app.model.req.search;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPostRequest {

  private String content;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
