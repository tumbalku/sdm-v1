package com.sdm.app.model.req.search;

import jakarta.validation.constraints.NotNull;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFileRequest {

  private String filename;
  private String fileType;
  private String type;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
