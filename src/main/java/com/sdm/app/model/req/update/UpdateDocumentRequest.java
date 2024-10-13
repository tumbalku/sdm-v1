package com.sdm.app.model.req.update;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDocumentRequest {

  @JsonIgnore
  private String id;
  private String name;
  private String description;
  private Integer priority;
}
