package com.sdm.app.enumrated;

public enum LetterType {
  KGB("Kenaikan Gaji Berkala"),
  SIP("Surat Izin Penelitian"),
  SPMT("Surat PMT"),
  AKREDITASI("Akreditasi");

  private final String description;

  LetterType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}

