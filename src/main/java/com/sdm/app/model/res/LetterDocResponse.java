package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterDocResponse {

  private String filename;
  private String type;
  private byte[] data;
}
