package com.sdm.app.model.req.create;

import com.sdm.app.enumrated.CutiStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCutiRequest {

  private LocalDate dateStart;
  private LocalDate dateEnd;
  private String number;
  private List<String> people;
  private Long kop;
  private String user;
  private String address;
  private String mark;
  private String message;
  private String reason;
  private CutiStatus status;
  private String workUnit;
  private String signedBy;
  private Integer total;
  private MultipartFile file;
}
