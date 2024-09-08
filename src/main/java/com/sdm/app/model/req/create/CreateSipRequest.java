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

  @NotNull(message = "nip tidak boleh null")
  @NotBlank(message = "nip tidak boleh kosong")
  private String nip;
  private LocalDate expiredAt;
  private MultipartFile file;
}
