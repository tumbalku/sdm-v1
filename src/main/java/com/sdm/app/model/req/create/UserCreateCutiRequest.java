package com.sdm.app.model.req.create;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


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
  private String workUnit;
  private String address;

}
