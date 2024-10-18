package com.sdm.app.model.req.create;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateCutiRequest {
  private LocalDate dateStart;
  private LocalDate dateEnd;
  @Size(min = 4, message = "minimum {min} character")
  private String reason;
  private Long kop;
  private Integer total;
  private String workUnit;
  private String address;
  private String forYear;
  private MultipartFile file;

}
