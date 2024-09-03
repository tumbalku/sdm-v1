package com.sdm.app;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class ExampleTest {

  @Test
  void testYear() {

    Year year = Year.now();
    System.out.println(year);
    String tahunLalu = "2024-08-21";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Parsing string menjadi objek LocalDate
    LocalDate date = LocalDate.parse(tahunLalu, formatter);
    Year convert = Year.of(date.getYear());
    System.out.println(convert);
  }
}
