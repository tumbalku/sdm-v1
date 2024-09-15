package com.sdm.app.model.req.create;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

  private String id;

  @Size(min = 6, message = "minimum {min} character")
  private String username;
  private String nip;
  private String name;
  private String email;
  private String phone;
  private String gender;
  private String address;
  private List<String> roles;

  // status in corporation
  private String pangkat;
  private String golongan;
  private String position;
  private String workUnit;


}
