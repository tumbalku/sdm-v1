package com.sdm.app.service.text;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.Kop;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.CutiStatus;
import com.sdm.app.repository.CutiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.sdm.app.service.text.PdfComponents.cmToPt;
import static com.sdm.app.service.text.PdfComponents.headerLandscape;
import static com.sdm.app.service.text.PdfUtils.*;

@Service
@AllArgsConstructor
public class PdfCutiReport {

  private final CutiRepository cutiRepository;

  public void makeCutiReports(Integer year) throws IOException {

    String output = "temp-pdf/cuti-reports.pdf";
    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    PageSize legalLandscape = PageSize.LEGAL.rotate();
    pdfDocument.setDefaultPageSize(legalLandscape);
    float width = legalLandscape.getWidth();
    System.out.println(width);

    // document
    Document document = new Document(pdfDocument);
    document.setTopMargin(cmToPt(2.54f));
    document.add(headerLandscape());
    document.add(doubleBorder(containerLandscape, 0.2f , Color.BLACK).setMarginBottom(0.8f).setMarginTop(2));
    document.add(doubleBorder(containerLandscape, 0.8f , Color.BLACK).setMarginBottom(20f));
    document.add(contentHead(year));
    document.add(contentBody(year).setMarginTop(10));

    document.close();

    System.out.println("Completed Make Cuti Reports");

  }

  private Table contentBody(int year){
    // Set table with 7 columns
    float[] columnWidths = {30, 210, 140, 190, 60, 60, 200, 70};
    // {NO, NAMA, JENIS CUTI/IZIN, TMT CUTI/IZIN, UNITKERJA, KETERANGAN}

    Table table = new Table(columnWidths);

    // Style border
    Border blackBorder = new SolidBorder(1);

    // Add header row (first row)

    table.addCell(new Cell(2, 1).add(setText("NO", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("NAMA", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("JENIS CUTI/IZIN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("NOMOR SURAT CUTI/IZIN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(1, 2).add(setText("TANGGAL CUTI/IZIN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("UNIT KERJA", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("KETERANGAN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

    table.addCell(new Cell().add(setText("MULAI", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell().add(setText("SELESAI", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

    // Add empty rows for the table body
    List<Cuti> cuti = cutiRepository.findByCreatedAtYearAndStatus(year, CutiStatus.APPROVE);

    int defaultLength = 25;
    int tableLength = Math.max(cuti.size(), defaultLength);
    for (int i = 0; i < tableLength; i++) {

      if(i < cuti.size()){
        Cuti data = cuti.get(i);
        User user = data.getUser();
        Kop kop = data.getKop();

        table.addCell(new Cell().add(String.valueOf(i + 1))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(user.getName(), 10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(kop.getType().getDescription(), 10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        String number = Objects.nonNull(data.getNumber()) && !data.getNumber().isBlank() ? data.getNumber() : "    ";
        table.addCell(new Cell().add(setText(
                String.format("%s/%s/%s/RSUD/%s/%s", kop.getUniKop(), number, kop.getType().getSort(), data.getRomawi(), data.getYear()), 10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(formatLocalDateddMMyyyy(data.getDateStart()), 10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(formatLocalDateddMMyyyy(data.getDateEnd()), 10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        String workUnit = Objects.nonNull(data.getWorkUnit()) ? data.getWorkUnit() : user.getWorkUnit();
        table.addCell(new Cell().add(setText(getValue(workUnit),10))
                .setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(String.format("%d Hari", data.getTotal()) , 10)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
      }else{
        table.addCell(new Cell().add(String.valueOf(i + 1)).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));
        table.addCell(new Cell().add("").setBorder(blackBorder));

      }
    }
    return table;
  }

  private Table contentHead(int year) {
    Table head = new Table(new float[]{percentPerWidth(containerLandscape, 12f / 12)});
    head.addCell(setText(String.format("BUKU CUTI/IZIN KEPEGAWAIAN TAHUN %d", year), 14).setBold().setTextAlignment(TextAlignment.CENTER));
    return head;
  }



}
