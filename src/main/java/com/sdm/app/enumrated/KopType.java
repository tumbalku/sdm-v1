package com.sdm.app.enumrated;

public enum KopType {
  SAKIT("Cuti Sakit", "SICS"),
  TAHUNAN("Cuti Tahunan","CT"),
  BERSALIN("Cuti Bersalin", "SCIB"),
  BESAR("Cuti Besar", "CB"),
  IZIN("Izin", "SI"),
  KARENA_ALASAN_PENTING("Cuti Karena Alasan Penting", "SCAP");

  private final String description;
  private final String sort;

  KopType(String description, String sort) {
    this.description = description;
    this.sort = sort;
  }

  public String getDescription() {
    return description;
  }
  public String getSort(){
    return sort;
  }



}
