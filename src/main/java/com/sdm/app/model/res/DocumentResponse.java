package com.sdm.app.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
  private String id;
  private String name;
  private String path;
  private Integer priority;
  private String description;
  private String type;
  private Integer size;
  private LocalDateTime uploadedAt;
  private LocalDateTime updatedAt;
}
