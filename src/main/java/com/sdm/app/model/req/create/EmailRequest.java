package com.sdm.app.model.req.create;

import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.res.CutiTypeCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

  private String name;
  private String nip;
  private String token;
  private String reason;
  private KopType type;
}
