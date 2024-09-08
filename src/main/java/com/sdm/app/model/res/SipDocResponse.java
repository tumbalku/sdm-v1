package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SipDocResponse {

  private String filename;
  private String type;
  private byte[] data;
}
