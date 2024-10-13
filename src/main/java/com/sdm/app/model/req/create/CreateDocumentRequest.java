package com.sdm.app.model.req.create;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDocumentRequest {

  private String name;
  private String description;
  private MultipartFile file;
}
