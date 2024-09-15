package com.sdm.app.model.req.create;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSipRequest {

  @JsonIgnore
  private String id;
  private String name;
  private String num;

  @NotNull(message = "userId tidak boleh null")
  @NotBlank(message = "userId tidak boleh kosong")
  private String userId;
  private LocalDate expiredAt;
  private MultipartFile file;
}
