package com.sdm.app.model.res;

import com.sdm.app.enumrated.KopType;
import lombok.*;

import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KopResponse {

  private Long id;

  private String name;
  private KopType type;

  private String uniKop;

  private String romawi;

  private Year year;
}
