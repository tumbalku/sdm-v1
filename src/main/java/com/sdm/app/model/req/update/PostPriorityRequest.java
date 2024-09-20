package com.sdm.app.model.req.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPriorityRequest {

  @JsonIgnore
  private String id;

  private Integer priority;
}
