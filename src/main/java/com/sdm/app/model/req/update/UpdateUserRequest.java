package com.sdm.app.model.req.update;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

  @Size(min = 6, message = "minimum {min} character")
  private String username;

  // social media
  private String phone;
  private String email;
  private String instagram;
  private String linkedin;
  private String twitter;
  private String facebook;

}
