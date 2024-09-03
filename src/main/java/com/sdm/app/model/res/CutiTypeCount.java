package com.sdm.app.model.res;

import com.sdm.app.enumrated.KopType;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CutiTypeCount {
  private KopType kop;
  private Long count;
}
