package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponseWithPagingReport<T, R> {

  private T data;
  private R report;

  private String message;

  private PagingResponse pagination;
}
