package com.sdm.app.enumrated;

public enum CutiStatus {

  APPROVE("Disetujui"),
  PENDING("Menunggu"),
  REJECT("Dibatalkan");

  private final String description;

  CutiStatus(String description) {
    this.description = description;

  }

  public String getDescription() {
    return description;
  }
}
