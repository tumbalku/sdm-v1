package com.sdm.app.enumrated;

public enum SipReportStatus {
  TERKIRIM("Terkirim"),
  TIDAK_TERKIRIM("Tidak terkirim");

  private final String description;

  SipReportStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
