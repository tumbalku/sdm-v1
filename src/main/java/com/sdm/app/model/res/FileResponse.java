package com.sdm.app.model.res;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {

  private String URL;
  private Integer size;
  private String contentType;

  private byte[] data;
}
