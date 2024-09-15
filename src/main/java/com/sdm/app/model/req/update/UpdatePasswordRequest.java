package com.sdm.app.model.req.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordRequest {

  @NotNull
  @Size(min = 6, message = "minimum {min} character")
  private String newPassword;

  @NotNull
  private String confirmPassword;
}
