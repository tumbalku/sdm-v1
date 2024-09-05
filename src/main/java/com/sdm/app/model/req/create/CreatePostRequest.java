package com.sdm.app.model.req.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

  @JsonIgnore
  private String id;
  private String title;
  private String content;
  private String imageUrl;
}
