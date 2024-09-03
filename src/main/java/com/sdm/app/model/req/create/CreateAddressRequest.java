package com.sdm.app.model.req.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {

  @JsonIgnore
  private Long id;

  @NotNull(message = "must not be null")
  @NotNull(message = "ot be blank")
  private String name;
}
