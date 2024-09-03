package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataReportResponse<T, R> {

  private T data;
  private R report;
}
