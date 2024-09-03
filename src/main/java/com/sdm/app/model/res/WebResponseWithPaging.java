package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponseWithPaging<T> {

  private T data;

  private String message;

  private PagingResponse pagination;
}
