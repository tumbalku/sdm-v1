package com.sdm.app.service.text;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class PdfUtils {


  protected static final float wFull = 595f;
  protected static final float hFull = 842f;
  protected static final float container = 570f;
  private static final String[] units = {
          "", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan", "sepuluh", "sebelas", "dua belas", "tiga belas", "empat belas", "lima belas", "enam belas", "tujuh belas", "delapan belas", "sembilan belas"
  };

  private static final String[] tens = {

          "", "", "dua puluh", "tiga puluh", "empat puluh", "lima puluh", "enam puluh", "tujuh puluh", "delapan puluh", "sembilan puluh"
  };

  public static String convert(long number) {
    if (number == 0) {
      return "nol";
    }

    if (number < 0) {
      return "minus " + convert(-number);
    }

    String words = "";

    if ((number / 100) > 0) {
      long hundreds = number / 100;
      if (hundreds == 1 && number % 100 == 0) {
        return "seratus";
      } else if (hundreds == 1) {
        words += "seratus ";
      } else {
        words += units[(int) hundreds] + " ratus ";
      }
      number %= 100;
    }

    if (number < 20) {
      words += units[(int) number];
    } else {
      long tensPlace = number / 10;
      words += tens[(int) tensPlace];
      if ((number % 10) > 0) {
        words += " " + units[(int) (number % 10)];
      }
    }

    return words.trim();
  }

  public static String convert(int number) {
    if (number == 0) {
      return "nol";
    }

    if (number < 0) {
      return "minus " + convert(-number);
    }

    String words = "";

    if ((number / 100) > 0) {
      if (number / 100 == 1 && number % 100 == 0) {
        return "seratus";
      } else if (number / 100 == 1) {
        words += "seratus ";
      } else {
        words += units[number / 100] + " ratus ";
      }
      number %= 100;
    }

    if (number < 20) {
      words += units[number];
    } else {
      words += tens[number / 10];
      if ((number % 10) > 0) {
        words += " " + units[number % 10];
      }
    }

    return words.trim();
  }

  public static Image image(String path) throws MalformedURLException {
    return new Image(ImageDataFactory.create(path));
  }

  public static Border border(float volume, Color color){

    return new SolidBorder(color, volume);
  }
  public static Table underline(float width, float volume, Color color){

    Border grayBorder = new SolidBorder(color, volume);
    Table borderDivideHeaderAndContent = new Table(new float[] {width});
    borderDivideHeaderAndContent.setBorder(grayBorder);

    return borderDivideHeaderAndContent;
  }
  public static float percentPerWidth(float current, float percent) {
    float result = current * percent;
    return Float.parseFloat(String.format("%.1f", result));
  }

  public static Cell setTextBold(String data){
    return new Cell().add(data)
            .setBorder(Border.NO_BORDER)
            .setBold();
  }
  public static  Cell setTextBold(String data, float size){
    return new Cell().add(data)
            .setBorder(Border.NO_BORDER)
            .setCharacterSpacing(1.5f)
            .setBold()
            .setFontSize(size);
  }
  public static  Cell setText(String data, float size){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setBorder(Border.NO_BORDER)
            .setFontSize(size);
  }
  public static  Cell setTextJustified(String data, float size){
    return new Cell().add(data)
            .setCharacterSpacing(1f)
            .setWordSpacing(10f)
//            .setTextAlignment(TextAlignment.JUSTIFIED)
            .setBorder(Border.NO_BORDER)
            .setFontSize(size);
  }
  public static  Cell setText(String data){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setBorder(Border.NO_BORDER);
  }

  public static  Cell tableHead(String data){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setBold()
            .setPadding(5);
  }
  public static  Cell tableHead(String data, float size){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setBold()
            .setPadding(5)
            .setFontSize(size);
  }

  public static  Cell tableHead(String data, float size, Color color, float colorOpacity){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setBold()
            .setPadding(5)
            .setBackgroundColor(color, colorOpacity)
            .setFontSize(size);
  }
  public static  Cell tableData(String data, float size){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setPadding(5)
            .setFontSize(size);
  }
  public static  Cell tableDataNoBorder(String data, float size){
    return new Cell().add(data)
            .setCharacterSpacing(0.8f)
            .setFontSize(size)
            .setBorder(Border.NO_BORDER);
  }
  public static  Cell tableDataBoldNoBorder(String data, float size){
    return new Cell().add(data)
            .setBold()
            .setCharacterSpacing(0.8f)
            .setFontSize(size)
            .setBorder(Border.NO_BORDER);
  }
  public static  Cell tableData(String data){
    return new Cell().add(data)
            .setPadding(5)
            .setCharacterSpacing(0.8f);
  }
  public static Table tableDataListKeyValue(Table table, Map<String, String> info){
    for (Map.Entry<String, String> entry : info.entrySet()) {
      table.addCell(tableDataNoBorder(entry.getKey(), 9).setPaddingLeft(20f));
      table.addCell(tableDataNoBorder(":", 9));
      if(entry.getKey().equalsIgnoreCase("nama")){
        table.addCell(tableDataBoldNoBorder(entry.getValue(), 9).setCharacterSpacing(1));
      }else{
        table.addCell(tableDataNoBorder(entry.getValue(), 9));
      }
    }
    return table;
  }

  public static void cellDataList(Table table, List<String> list){
    for (int i = 0; i < list.size(); i++) {
      table.addCell(new Cell().add(
                      tableData(String.format("%d. %s", i + 1, list.get(i)), 9)
                              .setPaddingTop(-2f)
                              .setPaddingBottom(-2f))
              .setBorder(Border.NO_BORDER));
    }
  }

  public static Table bigPoint(String number, String text){
    Table table = new Table(new float[]{20, 575});
    table.addCell(setText(number, 9).setPaddingTop(10f));
    table.addCell(setText(text, 9).setPaddingBottom(10f).setPaddingTop(10f));
    return table;
  }

  public static Table smallPoint(String number, String text){
    Table table = new Table(new float[]{20, 20, 555});
    table.addCell(setText(""));
    table.addCell(setText(number, 9));
    table.addCell(setText(text, 9));
    return table;
  }

  public static String capitalizeWords(String input) {
    // Memecah string menjadi kata-kata
    String[] words = input.split(" ");

    // Mengubah huruf pertama dari setiap kata menjadi kapital
    StringJoiner capitalizedWords = new StringJoiner(" ");
    for (String word : words) {
      // Mengambil huruf pertama dan mengubahnya menjadi kapital
      String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
      // Menambahkan kata yang telah dikapitalisasi ke StringJoiner
      capitalizedWords.add(capitalizedWord);
    }

    // Mengembalikan hasil sebagai string
    return capitalizedWords.toString();
  }

  public static String getValue(String data) {
    if (data == null || data.trim().isEmpty()) {
      return "-";
    }
    return data;
  }

}
