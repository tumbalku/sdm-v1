package com.sdm.app.model.req.search;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchSipRequest {

  private String identity;
  private String fileType;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
