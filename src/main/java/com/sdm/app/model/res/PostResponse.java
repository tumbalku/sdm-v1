package com.sdm.app.model.res;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

  private String id;
  private String title;
  private String content;
  private UserLite user;
  private String imageUrl;
  private Integer priority;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
