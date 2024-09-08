package com.sdm.app.enumrated;

public enum SipReportStatus {
  TERKIRIM("Terkirim"),
  TIDAK_TERKIRI("Tidak terkirim");

  private final String description;

  SipReportStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
