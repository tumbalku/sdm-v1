package com.sdm.app.service.text;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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
import com.sdm.app.service.impl.IdsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sdm.app.service.text.PdfComponents.cmToPt;
import static com.sdm.app.service.text.PdfComponents.headerPortrait;
import static com.sdm.app.service.text.PdfUtils.*;
@Service
@AllArgsConstructor
public class KartuKontrolPdf {

  private final IdsServiceImpl idsService;
  private final CutiRepository cutiRepository;

  public void makeMyCutiReport(User user) throws IOException {

    String output = "temp-pdf/kartu-kontrol.pdf";

    FontProgram fontProgram = FontProgramFactory.createFont("arial-font/arial.ttf");
    PdfFont fontArial = PdfFontFactory.createFont(
            fontProgram, PdfEncodings.WINANSI, true);
    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    pdfDocument.setDefaultPageSize(PageSize.LEGAL);

    float margin = cmToPt(1f);
    // document
    Document document = new Document(pdfDocument);
    document.setFont(fontArial);
    document.setMargins(cmToPt(2.54f), margin, margin, cmToPt(2.54f));
    document.add(headerPortrait());
    document.add(doubleBorder(container, 0.5f , Color.BLACK).setMarginBottom(1f).setMarginTop(2));
    document.add(doubleBorder(container, 1f , Color.BLACK).setMarginBottom(20f));
    document.add(contentHead(user));
    document.add(contentBody(user).setMarginTop(10));

    document.close();

    System.out.println("Completed Make My Cuti Report");

  }

  private  Table contentBody(User user){
    // Set table with 7 columns
    float[] columnWidths = {30, 140, 60, 60, 110, 80, 60, 70};
    Table table = new Table(columnWidths);

    // Style border
    Border blackBorder = new SolidBorder(1);

    // Add header row (first row)
    table.addCell(new Cell(2, 1).add(setText("NO", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("JENIS CUTI/IZIN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(1, 2).add(setText("TMT CUTI/IZIN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("UNIT KERJA", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(1, 2).add(setText("PARAF", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell(2, 1).add(setText("KETERANGAN", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

    // Add second row (for "TMT CUTI/IZIN" and "PARAF")
    table.addCell(new Cell().add(setText("MULAI", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell().add(setText("SELESAI", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell().add(setText("ATASAN LANGSUNG", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
    table.addCell(new Cell().add(setText("PEJABAT TERKAIT", 10).setBold()).setBorder(blackBorder).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

    // Add empty rows for the table body
    List<Cuti> cuti = cutiRepository.findByUserAndStatus(user, CutiStatus.APPROVE);

    int defaultLength = 25;
    int tableLength = Math.max(cuti.size(), defaultLength);
    for (int i = 0; i < tableLength; i++) {

      if(i < cuti.size()){
        Cuti data = cuti.get(i);
        Kop kop = data.getKop();
        table.addCell(new Cell().add(String.valueOf(i + 1)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(kop.getType().getDescription(), 10)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(formatLocalDateddMMyyyy(data.getDateStart()), 10)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add(setText(formatLocalDateddMMyyyy(data.getDateEnd()), 10)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        String workUnit = Objects.nonNull(data.getWorkUnit()) ? data.getWorkUnit() : user.getWorkUnit();
        table.addCell(new Cell().add(setText(getValue(workUnit),10)).setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add("").setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
        table.addCell(new Cell().add("").setBorder(blackBorder)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell().add(setText(String.format("%d Hari", calculateDaysBetween(data.getDateStart(), data.getDateEnd())) , 10)).setBorder(blackBorder)
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

  private  Table contentHead(User user) {
    Table head = new Table(new float[]{percentPerWidth(container, 12f / 12)});
    head.addCell(setText("KARTU KONTROL CUTI/IZIN PEGAWAI", 14).setBold().setTextAlignment(TextAlignment.CENTER));

    Table userDetail = new Table(new float[]{150, 10, 435});
    userDetail.addCell(setText("Nama", 12).setBold());
    userDetail.addCell(setText(":", 12));
    userDetail.addCell(setText(getValue(user.getName()), 12));
    userDetail.addCell(setText("Nip", 12).setBold());
    userDetail.addCell(setText(":", 12));
    userDetail.addCell(setText(getValue(user.getNip()), 12));

    head.addCell(new Cell().add(userDetail.setMarginTop(5)).setBorder(Border.NO_BORDER));
    return head;
  }

}
