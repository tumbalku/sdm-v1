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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.Kop;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.service.impl.IdsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.sdm.app.service.text.PdfComponents.cmToPt;
import static com.sdm.app.service.text.PdfComponents.headerPortrait;
import static com.sdm.app.service.text.PdfUtils.*;

@Service
@AllArgsConstructor
public class CutiPdfService {

  private final IdsServiceImpl idsService;

  public void makeAnCutiReport(Cuti cuti) throws IOException {
    User user = cuti.getUser();
    String output = "temp-pdf/output.pdf";

    FontProgram fontProgram = FontProgramFactory.createFont("arial-font/arial.ttf");
    PdfFont fontArial = PdfFontFactory.createFont(
            fontProgram, PdfEncodings.WINANSI, true);
    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    pdfDocument.setDefaultPageSize(PageSize.LEGAL);

    KopType kop = cuti.getKop().getType();

    Table core;
    if (Objects.requireNonNull(kop) == KopType.IZIN) {
      core = coreIzin(user, cuti);
    } else {
      core = core(user, cuti);
    }

    float margin = cmToPt(2.54f);

    // document
    Document document = new Document(pdfDocument);
    document.setFont(fontArial);
    document.setMargins(cmToPt(1.52f), margin, margin, margin);
    document.add(headerPortrait());
    document.add(doubleBorder(container, 0.5f, Color.BLACK).setMarginBottom(1f).setMarginTop(2));
    document.add(doubleBorder(container, 1f, Color.BLACK).setMarginBottom(20f));
    document.add(core);
    document.add(signed(cuti));
    document.add(tembusan(cuti));

    document.close();

    System.out.println("Completed create cuti detail report ");
  }

  private Table coreIzin(User user, Cuti cuti) {
    Table core = new Table(new float[]{wFull});

    Kop kop = cuti.getKop();

    core.addCell(setText("SURAT - IZIN", 12)
            .setBold()
            .setUnderline()
            .setTextAlignment(TextAlignment.CENTER));
    String cutiNum = Objects.nonNull(cuti.getNumber()) &&
            !cuti.getNumber().isBlank() ?
            cuti.getNumber() : "\t\t";

    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
            kop.getUniKop(),
            cutiNum,
            kop.getType().getSort(),
            kop.getRomawi(),
            cuti.getYear().getValue()), 12)
            .setPadding(-2f)
            .setPaddingBottom(10f)
            .setTextAlignment(TextAlignment.CENTER));


    core.addCell(new Cell().add(bigPoint(" ", "Yang bertanda tangan dibawah ini menerangkan bahwa"))
            .setBorder(Border.NO_BORDER));

    float[] userInfo = {185f, 10, 400};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);


    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if (Objects.nonNull(user.getNip())) {
      userDetail.put("NIP", user.getNip());
    }

    String pangkat = user.getPangkat();
    String golongan = user.getGolongan();

    if (Objects.nonNull(pangkat) && !pangkat.isBlank()) {
      String rankGroup = getValue(pangkat) + "/ " + getValue(golongan);
      userDetail.put("Pangkat / Golongan", rankGroup);
    } else {
      String checkGol = Objects.nonNull(golongan) ? golongan : "Kontrak BLUD";
      userDetail.put("Golongan", checkGol);
    }

    userDetail.put("Jabatan", getValue(user.getPosition()));

    String workUnit = Objects.nonNull(cuti.getWorkUnit()) && !cuti.getWorkUnit().isBlank() ?
            cuti.getWorkUnit() : user.getWorkUnit();
    System.out.println(workUnit);
    userDetail.put("Unit kerja", getValue(workUnit));
    userDetail.put("Untuk keperluan", getValue(cuti.getReason()));

    String dateStart = cuti.getDateStart().format(dateDayFormatter());
    String dateEnd = cuti.getDateEnd().format(dateFormatter());

    userDetail.put("Jangka waktu", String.format("%d (%s) hari terhitung mulai tanggal %s s/d %s",
            cuti.getTotal(), convert(cuti.getTotal()), dateStart, dateEnd)
    );

    String address =
            Objects.nonNull(cuti.getAddress()) ?
                    cuti.getAddress() :
                    Objects.nonNull(user.getAddress()) ?
                            user.getAddress().getName() : "-";

    userDetail.put("Alamat selama izin", address);

    Table userInfoTbl = tableDataListKeyValue(tblUserInfo, userDetail);
    core.addCell(new Cell().add(userInfoTbl).setBorder(Border.NO_BORDER));

    core.addCell(new Cell().add(bigPoint(" ", "Demikian Surat Izin ini diberikan untuk dapat dipergunakan sebagaimana mestinya."))
            .setBorder(Border.NO_BORDER));
    return core;
  }

  private Table core(User user, Cuti cuti) {
    Table core = new Table(new float[]{wFull});

    Kop kop = cuti.getKop();

    String cutiName = kop.getType().getDescription();

    // core title
    core.addCell(setText(String.format("SURAT IZIN %s", cutiName.toUpperCase()), 12)
            .setBold()
            .setUnderline()
            .setTextAlignment(TextAlignment.CENTER));

    String cutiNum = Objects.nonNull(cuti.getNumber()) &&
            !cuti.getNumber().isBlank() ?
            cuti.getNumber() : String.format("%10s", "");

    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
            kop.getUniKop(),
            cutiNum,
            kop.getType().getSort(),
            kop.getRomawi(),
            cuti.getYear().getValue()), 12)
            .setPadding(-2f)
            .setPaddingBottom(20f)
            .setTextAlignment(TextAlignment.CENTER));


    String status = Objects.nonNull(user.getNip()) ? "Pegawai Negeri Sipil" : "Pegawai";
    String forYear = Objects.nonNull(cuti.getForYear()) &&
            !cuti.getForYear().isBlank() ?
            cuti.getForYear() : cuti.getYear().toString();
    if (kop.getType().equals(KopType.TAHUNAN)) {
      core.addCell(new Cell().add(bigPoint("1.",
                      String.format("Diberikan %s untuk tahun %s kepada %s:", capitalizeWords(cutiName), forYear, status)))
              .setBorder(Border.NO_BORDER));
    } else {
      core.addCell(new Cell().add(bigPoint("1.",
                      String.format("Diberikan %s kepada %s:", capitalizeWords(cutiName), status)))
              .setBorder(Border.NO_BORDER));
    }

    float[] userInfo = {180f, 10, 405};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);


    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if (Objects.nonNull(user.getNip())) {
      userDetail.put("NIP", user.getNip());
    }

    String pangkat = user.getPangkat();
    String golongan = user.getGolongan();

    if (Objects.nonNull(pangkat) && !pangkat.isBlank()) {
      String rankGroup = getValue(pangkat) + "/ " + getValue(golongan);
      userDetail.put("Pangkat / Golongan", rankGroup);
    } else {
      String checkGol = Objects.nonNull(golongan) ? golongan : "Kontrak BLUD";
      userDetail.put("Golongan", checkGol);
    }

    String workUnit = Objects.nonNull(cuti.getWorkUnit()) && !cuti.getWorkUnit().isBlank() ?
            cuti.getWorkUnit() : user.getWorkUnit();
    userDetail.put("Jabatan", getValue(user.getPosition()));
    userDetail.put("Unit Kerja", getValue(workUnit));

    String address = Objects.nonNull(cuti.getAddress()) && !cuti.getAddress().isBlank() ? cuti.getAddress() :
            Objects.nonNull(user.getAddress()) ? user.getAddress().getName() : "-";

    userDetail.put("Alamat selama Cuti", address);

    Table userInfoTbl = tableDataListKeyValue(tblUserInfo, userDetail);
    core.addCell(new Cell().add(userInfoTbl).setBorder(Border.NO_BORDER));

    String dateStart = cuti.getDateStart().format(dateFormatter());
    String dateEnd = cuti.getDateEnd().format(dateFormatter());

    core.addCell(setText(
            String.format("Selama %d (%s) hari kerja terhitung mulai tanggal %s s/d %s dengan ketentuan sebagai berikut:",
                    cuti.getTotal(), convert(cuti.getTotal()), dateStart, dateEnd), 12
    ).setPaddingLeft(20f).setPaddingTop(10f).setTextAlignment(TextAlignment.JUSTIFIED));

    core.addCell(new Cell().add(smallPoint("a.",
                    String.format("Sebelum menjalankan %s wajib menyerahkan pekerjaannya kepada atasan langsungnya atau pejabat yang ditunjuk.", cutiName)))
            .setBorder(Border.NO_BORDER));

    if (kop.getType().equals(KopType.BERSALIN)) {
      core.addCell(new Cell().add(smallPoint("b.", "Segera setelah persalinan yang bersangkutan supaya memberitahukan tanggal persalinan kepada pejabat yang berwenang memberikan cuti"))
              .setBorder(Border.NO_BORDER));
      core.addCell(new Cell().add(smallPoint("c.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    } else {
      core.addCell(new Cell().add(smallPoint("b.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    }


    core.addCell(new Cell().add(bigPoint("2.",
                    String.format("Demikian Surat Izin %s ini dibuat untuk dipergunakan sebagaimana mestinya.", capitalizeWords(cutiName))))
            .setBorder(Border.NO_BORDER));
    return core;
  }

  private Table signed(Cuti cuti) {

    float[] ttdSize = {280f, 25, 290};
    Table tblTTD = new Table(ttdSize);

    User user = idsService.getUser(cuti.getSignedBy());
    String rank = Objects.nonNull(user.getPangkat()) ? user.getPangkat() + ", " + user.getGolongan() : user.getGolongan();
    String nip = Objects.nonNull(user.getNip()) ? user.getNip() : "-";
    String mark = Objects.nonNull(cuti.getMark()) && !cuti.getMark().equalsIgnoreCase("Direktur") ? cuti.getMark().concat(".") : "";

    // fist column
    tblTTD.addCell(new Cell().setBorder(Border.NO_BORDER));

    // second column
    Table ttd2 = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd2.addCell(setText(mark, 12).setBold().setPaddingTop(38f));
    tblTTD.addCell(new Cell().add(ttd2).setBorder(Border.NO_BORDER));

    String formatTddDate = cuti.getUpdatedAt().format(dateFormatter());
    String ttdDate = String.format("Kendari, %s", formatTddDate);

    Table ttd = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd.addCell(setText(ttdDate, 12).setPaddingTop(15f).setPaddingBottom(5));
    ttd.addCell(setText("Direktur,", 12).setBold().setPaddingBottom(60f));
    ttd.addCell(setText(user.getName(), 12).setBold().setUnderline());
    ttd.addCell(setText(rank, 12).setPaddingTop(-5f));
    ttd.addCell(setText("NIP. " + nip, 12).setPaddingTop(-4f));

    tblTTD.addCell(new Cell().add(ttd).setBorder(Border.NO_BORDER));
    return tblTTD;
  }

  private Table tembusan(Cuti cuti) {
    Table tembusan = new Table(new float[]{wFull});
    Table tembusanHeader = new Table(new float[]{85, 510});

    tembusanHeader.addCell(setText("Tembusan :", 12).setBold().setUnderline());
//    tembusanHeader.addCell(setText("Disampaikan Kepada Yth,", 12).setPaddingTop(1).setPaddingLeft(-12));
    tembusan.addCell(new Cell().add(tembusanHeader).setBorder(Border.NO_BORDER).setPaddingTop(30f).setPaddingBottom(-2f));

    List<String> tembusanList = new LinkedList<>();


    if (cuti.getPeople().size() != 0) {
      cuti.getPeople().forEach(people -> tembusanList.add(people.getName().concat(";")));
    }
    tembusanList.add("Yang bersangkutan untuk diketahui;");
    tembusanList.add("Pertinggal.");

    cellDataList(tembusan, tembusanList);
    return tembusan;
  }

}
