package com.sdm.app.service.text;


import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.sdm.app.entity.Cuti;
import com.sdm.app.entity.Kop;
import com.sdm.app.entity.User;
import com.sdm.app.enumrated.KopType;
import com.sdm.app.service.impl.IdsServiceImpl;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.sdm.app.service.text.PdfUtils.*;

@Service
@AllArgsConstructor
public class CutiPdfService {

  private final IdsServiceImpl idsService;

  public void makeAnCutiReport(Cuti cuti) throws IOException {
    User user = cuti.getUser();
    String output = "temp-pdf/output.pdf";

    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    pdfDocument.setDefaultPageSize(PageSize.LEGAL);

    Table header = header();

    KopType kop = cuti.getKop().getType();

    Table core;
    if (Objects.requireNonNull(kop) == KopType.IZIN) {
      core = coreIzin(user, cuti);
    } else {
      core = core(user, cuti);
    }

    Table signed = signed(cuti);
    Table tembusan = tembusan(cuti);

    // document
    Document document = new Document(pdfDocument);
    document.add(header);
    document.add(doubleBorder(container, 0.1f , Color.BLACK).setMarginBottom(2f).setMarginTop(2));
    document.add(doubleBorder(container, 0.5f , Color.BLACK).setMarginBottom(20f));
    document.add(core);
    document.add(signed);
    document.add(tembusan);

    document.close();

    System.out.println("Completed");
  }

  private Table header() throws IOException {
    Table header = new Table(new float[]{
            percentPerWidth(container, 2.0f / 12),
            percentPerWidth(container, 10.0f / 12),
    });

//    FontProgram mainFont = FontProgramFactory.createFont("KaushanScript-Regular.ttf");
//    PdfFont font = PdfFontFactory.createFont(
//            mainFont, PdfEncodings.WINANSI, true);

    // header title
    Table headerTitle = new Table(new float[]{percentPerWidth(container, 12f / 12)});

    headerTitle.addCell(setText("PEMERINTAH PROVINSI SULAWESI TENGGARA", 12f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-5.f)
    );
    headerTitle.addCell(setText("RUMAH SAKIT UMUM DAERAH BAHTERAMAS", 16f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-5.f)
    );
    headerTitle.addCell(setText("Jalan Kapten Piere Tandean No. 50 Telp. (0401) 3195611 Baruga Kendari", 9f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-5.f)
    );
    headerTitle.addCell(setText("Email : admin@rsud-bahteramas.go.id Website : www.rsud-bahteramas.go.id", 9f)
            .setTextAlignment(TextAlignment.CENTER)
    );

    header.addCell(new Cell().add(image("logo.png")
                    .setWidth(60f)
                    .setHeight(60f))
            .setBorder(Border.NO_BORDER)
            .setPaddingLeft(25)
    );
    header.addCell(new Cell().add(headerTitle).setPaddingLeft(-50f).setBorder(Border.NO_BORDER));

    return header;
  }
  private Table coreIzin(User user, Cuti cuti){
    Table core = new Table(new float[]{wFull});

    Kop kop = cuti.getKop();

    core.addCell(setText("SURAT - IZIN", 12)
            .setBold()
            .setUnderline()
            .setTextAlignment(TextAlignment.CENTER));
    String cutiNum = Objects.nonNull(cuti.getNumber()) ? cuti.getNumber() : "  ";
    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
            kop.getUniKop(),
            cutiNum,
            kop.getType().getSort(),
            kop.getRomawi(),
            cuti.getYear().getValue()),12)
            .setPadding(-2f)
            .setPaddingBottom(10f)
            .setTextAlignment(TextAlignment.CENTER));


    core.addCell(new Cell().add(bigPoint(" ", "Yang bertanda tangan dibawah ini menerangkan bahwa"))
            .setBorder(Border.NO_BORDER));

    float [] userInfo = {185f, 10, 400};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);


    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if(Objects.nonNull(user.getNip())){
      userDetail.put("NIP", user.getNip());
    }

    String pangkat = user.getPangkat();
    String golongan = user.getGolongan();

    if(Objects.nonNull(pangkat) && !pangkat.isBlank()){
      String rankGroup = getValue(pangkat) + "/ " + getValue(golongan);
      userDetail.put("Pangkat / Gologan", rankGroup);
    } else {
      String checkGol = Objects.nonNull(golongan) ? golongan : "Kontrak BLUD";
      userDetail.put("Gologan", checkGol);
    }

    userDetail.put("Jabatan", getValue(user.getPosition()));
    userDetail.put("Unit kerja", getValue(user.getWorkUnit()));
    userDetail.put("Untuk keperluan", getValue(cuti.getReason()));

    String dateStart = cuti.getDateStart().format(dateDayFormatter());
    String dateEnd = cuti.getDateEnd().format(dateFormatter());

    long daysBetween = ChronoUnit.DAYS.between(cuti.getDateStart(), cuti.getDateEnd());

    userDetail.put("Jangka waktu", String.format("%d (%s) hari terhitung mulai tanggal %s s/d %s",
        daysBetween, convert(daysBetween), dateStart, dateEnd)
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
  private Table core(User user, Cuti cuti){
    Table core = new Table(new float[]{wFull});

    Kop kop = cuti.getKop();

    String cutiName = kop.getType().getDescription();

    // core title
    core.addCell(setText(String.format("SURAT IZIN %s", cutiName.toUpperCase()), 12)
            .setBold()
            .setUnderline()
            .setTextAlignment(TextAlignment.CENTER));

    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
            kop.getUniKop(),
            cuti.getNumber(),
            kop.getType().getSort(),
            kop.getRomawi(),
            cuti.getYear().getValue()),12)
            .setPadding(-2f)
            .setPaddingBottom(30f)
            .setTextAlignment(TextAlignment.CENTER));


    String status = Objects.nonNull(user.getNip()) ? "Pegawai Negeri Sipil" : "Pegawai";
    core.addCell(new Cell().add(bigPoint("1.",
                    String.format("Diberikan %s Kepada %s:", capitalizeWords(cutiName), status)))
            .setBorder(Border.NO_BORDER));

    float [] userInfo = {180f, 10, 405};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);


    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if(Objects.nonNull(user.getNip())){
      userDetail.put("NIP", user.getNip());
    }

    String pangkat = user.getPangkat();
    String golongan = user.getGolongan();

    if(Objects.nonNull(pangkat) && !pangkat.isBlank()){
      String rankGroup = getValue(pangkat) + "/ " + getValue(golongan);
      userDetail.put("Pangkat / Gologan", rankGroup);
    }else{
      String checkGol = Objects.nonNull(golongan) ? golongan : "Kontrak BLUD";
      userDetail.put("Gologan", checkGol);
    }

    userDetail.put("Jabatan", getValue(user.getPosition()));
    userDetail.put("Unit Kerja", getValue(user.getWorkUnit()));

    String address =
            Objects.nonNull(cuti.getAddress()) ?
                    cuti.getAddress() :
                    Objects.nonNull(user.getAddress()) ?
                            user.getAddress().getName() : "-";

    userDetail.put("Alamat selama Cuti", address);

    Table userInfoTbl = tableDataListKeyValue(tblUserInfo, userDetail);
    core.addCell(new Cell().add(userInfoTbl).setBorder(Border.NO_BORDER));

    String dateStart = cuti.getDateStart().format(dateFormatter());
    String dateEnd = cuti.getDateEnd().format(dateFormatter());

    long daysBetween = ChronoUnit.DAYS.between(cuti.getDateStart(), cuti.getDateEnd());

    core.addCell(setText(
            String.format("Selama %d (%s) hari terhitung mulai tanggal %s s/d %s dengan ketentuan sebagai berikut:",
                    daysBetween, convert(daysBetween), dateStart, dateEnd), 12
    ).setPaddingLeft(20f).setPaddingTop(10f).setTextAlignment(TextAlignment.JUSTIFIED));

    core.addCell(new Cell().add(smallPoint("a.",
                    String.format("Sebelum menjalankan %s wajib menyerahkan pekerjaannya kepada atasan langsungnya atau pejabat yang ditunjuk.", cutiName)))
            .setBorder(Border.NO_BORDER));

    if(kop.getType().equals(KopType.BERSALIN)){
      core.addCell(new Cell().add(smallPoint("b.","Segera setelah persalinan yang bersangkutan supaya memberitahukan tanggal persalinan kepada pejabat yang berwenang memberikan cuti"))
              .setBorder(Border.NO_BORDER));
      core.addCell(new Cell().add(smallPoint("c.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    }else{
      core.addCell(new Cell().add(smallPoint("b.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    }


    core.addCell(new Cell().add(bigPoint("2.",
                    String.format("Demikian Surat Izin %s ini dibuat untuk dipergunakan sebagaimana mestinya.", capitalizeWords(cutiName))))
            .setBorder(Border.NO_BORDER));
    return core;
  }
  private Table signed(Cuti cuti){

    float [] ttdSize = {280f, 25, 290};
    Table tblTTD = new Table(ttdSize);

    User user = idsService.getUser(cuti.getSignedBy());
    String rank = Objects.nonNull(user.getPangkat()) ? user.getPangkat() + " " + user.getGolongan() : user.getGolongan();
    String nip = Objects.nonNull(user.getNip()) ? user.getNip() : "-";
    String mark = Objects.nonNull(cuti.getMark()) && !cuti.getMark().equalsIgnoreCase("Direktur") ? cuti.getMark().concat(".") : "";

    // fist column
    tblTTD.addCell(new Cell().setBorder(Border.NO_BORDER));

    // second column
    Table ttd2 = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd2.addCell(setTextBold(mark, 12).setPaddingTop(36.7f));
    tblTTD.addCell(new Cell().add(ttd2).setBorder(Border.NO_BORDER));

    String formatTddDate = cuti.getCreatedAt().format(dateFormatter());
    String ttdDate = String.format("Kendari, %s", formatTddDate);

    Table ttd = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd.addCell(setText(ttdDate, 12).setPaddingTop(15f));
    ttd.addCell(setTextBold("Direktur,", 12).setPaddingBottom(60f));
    ttd.addCell(setTextBold(user.getName(), 12).setUnderline());
    ttd.addCell(setText(rank, 12).setPaddingTop(-3f));
    ttd.addCell(setText("NIP. " + nip, 12).setPaddingTop(-3f));

    tblTTD.addCell(new Cell().add(ttd).setBorder(Border.NO_BORDER));
    return tblTTD;
  }
  private Table tembusan(Cuti cuti){
    Table tembusan = new Table(new float[]{wFull});
    Table tembusanHeader = new Table(new float[]{85, 510});

    tembusanHeader.addCell(setTextBold("Tembusan :", 12).setUnderline());
    tembusanHeader.addCell(setText("Disampaikan Kepada Yth,", 12).setPaddingLeft(5f));
    tembusan.addCell(new Cell().add(tembusanHeader).setBorder(Border.NO_BORDER).setPaddingTop(30f));

    List<String> tembusanList = new LinkedList<>();


    if(cuti.getPeople().size() != 0){
      cuti.getPeople().forEach(people -> tembusanList.add(people.getName().concat(";")));
    }
    tembusanList.add("Yang Bersangkutan Untuk diketahui;");
    tembusanList.add("Pertinggal.");

    cellDataList(tembusan, tembusanList);
    return tembusan;
  }

  public void makeAnCutiReportXXX(Cuti cuti) throws FileNotFoundException, MalformedURLException {

    User user = cuti.getUser();
    String output = "temp-pdf/output.pdf";

    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    pdfDocument.setDefaultPageSize(PageSize.A4);

    Table header = new Table(new float[]{
            percentPerWidth(container, 2.0f / 12),
            percentPerWidth(container, 10.0f / 12),
    });

    // header title
    Table headerTitle = new Table(new float[]{percentPerWidth(container, 12f / 12)});

    headerTitle.addCell(setTextBold("PEMERINTAH PROVINSI SULAWESI TENGGARA", 12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
            .setPaddingTop(8f)
    );
    headerTitle.addCell(setTextBold("RUMAH SAKIT UMUM DAERAH BAHTERAMAS", 16f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
    );
    headerTitle.addCell(setText("Jalan Kapten Piere Tandean No. 50 Telp. (0401) 3195611 Baruga Kendari", 9f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
    );
    headerTitle.addCell(setText("Email: admin@rsud-bahteramas.go.id Website: www.rsud-bahteramas.go.id", 9f)
            .setTextAlignment(TextAlignment.CENTER)
            .setCharacterSpacing(0.5f)
    );
    // -------------

    // header
    header.addCell(new Cell().add(image("logo-anoa-sultra.png")
                    .setWidth(95f)
                    .setHeight(70f))
            .setBorder(Border.NO_BORDER)
    );
    header.addCell(new Cell().add(headerTitle).setBorder(Border.NO_BORDER));

    // -------------------------------------------------------------------------

    Table core = new Table(new float[]{wFull});


    Kop kop = cuti.getKop();

    String cutiName = kop.getType().getDescription();

    // core title
    if(kop.getType().equals(KopType.IZIN)){
      core.addCell(setTextBold("SURAT - IZIN", 9)
              .setUnderline()
              .setTextAlignment(TextAlignment.CENTER));
    }else{
      core.addCell(setTextBold(String.format("SURAT IZIN %s", cutiName.toUpperCase()), 9)
              .setUnderline()
              .setTextAlignment(TextAlignment.CENTER));
    }


    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
                kop.getUniKop(),
                cuti.getNumber(),
                kop.getType().getSort(),
                kop.getRomawi(),
                cuti.getYear().getValue()),9)
            .setPadding(-2f)
            .setPaddingBottom(30f)
            .setTextAlignment(TextAlignment.CENTER));


    core.addCell(new Cell().add(bigPoint("1.",
                    String.format("Diberikan %s Kepada Pegawai Negeri Sipil:", capitalizeWords(cutiName))))
            .setBorder(Border.NO_BORDER));

    float [] userInfo = {195f, 10, 390};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);


    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if(Objects.nonNull(user.getNip())){
      userDetail.put("NIP", user.getId());
    }

    String pangkat = user.getPangkat();
    String golongan = user.getGolongan();

    if(Objects.nonNull(pangkat) && pangkat.isBlank()){
      String rankGroup = getValue(pangkat) + "/ " + getValue(golongan);
      userDetail.put("Pangkat / Gologan", rankGroup);
    }else{
      userDetail.put("Gologan", getValue(golongan));
    }
    userDetail.put("Jabatan", getValue(user.getPosition()));
    userDetail.put("Unit Kerja", getValue(user.getWorkUnit()));

    String address =
            Objects.nonNull(cuti.getAddress()) ?
                    cuti.getAddress() :
            Objects.nonNull(user.getAddress()) ?
                    user.getAddress().getName() : "-";

    userDetail.put("Alamat selama Cuti", address);

    Table userInfoTbl = tableDataListKeyValue(tblUserInfo, userDetail);
    core.addCell(new Cell().add(userInfoTbl).setBorder(Border.NO_BORDER));


    // cuti detail
    Locale local = new Locale("id", "ID");
    String pattern = "dd MMMM yyyy";
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern, local);

    LocalDate cutiDateStart = cuti.getDateStart();
    LocalDate cutiDateEnd = cuti.getDateEnd();

    String dateStart = cutiDateStart.format(dateFormatter);
    String dateEnd = cutiDateEnd.format(dateFormatter);

    long daysBetween = ChronoUnit.DAYS.between(cuti.getDateStart(), cuti.getDateEnd());

    core.addCell(setText(
            String.format("Selama %d (%s) hari terhitung mulai tanggal %s s/d %s dengan ketentuan sebagai berikut:",
                    daysBetween, convert(daysBetween), dateStart, dateEnd), 9
    ).setPaddingLeft(20f).setPaddingTop(10f));

    core.addCell(new Cell().add(smallPoint("a.",
            String.format("Sebelum menjalankan %s wajib menyerahkan pekerjaannya kepada atasan langsungnya atau pejabat yang ditunjuk.", cutiName)))
            .setBorder(Border.NO_BORDER));

    if(kop.getType().equals(KopType.BERSALIN)){
      core.addCell(new Cell().add(smallPoint("b.","Segera setelah persalinan yang bersangkutan supaya memberitahukan tanggal persalinan kepada pejabat yang berwenang memberikan cuti"))
              .setBorder(Border.NO_BORDER));
      core.addCell(new Cell().add(smallPoint("c.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    }else{
      core.addCell(new Cell().add(smallPoint("b.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", cutiName)))
              .setBorder(Border.NO_BORDER));
    }


    core.addCell(new Cell().add(bigPoint("2.",
            String.format("Demikian Surat Izin %s ini dibuat untuk dipergunakan sebagaimana mestinya.", capitalizeWords(cutiName))))
            .setBorder(Border.NO_BORDER));

    // Table tanda tanggan
    float [] ttdSize = {330f, 265};
    Table tblTTD = new Table(ttdSize);
    tblTTD.addCell(new Cell().setBorder(Border.NO_BORDER));
    LocalDateTime cutiConfirmDate = cuti.getCreatedAt();

    String formatTddDate = cutiConfirmDate.format(dateFormatter);
    String ttdDate = String.format("Kendari, %s", formatTddDate);
    // actual TTD

    //
    Table ttd = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd.addCell(setText(ttdDate, 9).setPaddingTop(50f));
    ttd.addCell(setTextBold("Direktur,", 8).setPaddingBottom(60f));
    ttd.addCell(setTextBold("dr. H. Hasmudin, Sp.B", 8).setUnderline());
    ttd.addCell(setText("Pembina Tk I. Gol IV/ ", 8).setPaddingTop(-3f));
    ttd.addCell(setText("NIP. 1234567880123", 8).setPaddingTop(-3f));

    tblTTD.addCell(new Cell().add(ttd).setBorder(Border.NO_BORDER));

    Table tembusan = new Table(new float[]{wFull});
    Table tembusanHeader = new Table(new float[]{70,525});

    tembusanHeader.addCell(setTextBold("Tembusan :", 8).setUnderline());
    tembusanHeader.addCell(setText("Disampaikan Kepada Yth,", 9).setPaddingLeft(-5f));
    tembusan.addCell(new Cell().add(tembusanHeader).setBorder(Border.NO_BORDER).setPaddingTop(30f));

    List<String> tembusanList = new LinkedList<>();


    if(cuti.getPeople().size() != 0){
      cuti.getPeople().forEach(people -> tembusanList.add(people.getName().concat(";")));
    }
    tembusanList.add("Yang bersangkutan untuk diketahui;");
    tembusanList.add("Pertinggal.");

    cellDataList(tembusan, tembusanList);


    // document
    Document document = new Document(pdfDocument);
    document.add(header);
    document.add(underline(container, 0.9f , Color.BLACK).setMarginBottom(20f));
    document.add(core);
    document.add(tblTTD);
    document.add(tembusan);

    document.close();

    System.out.println("Completed");
  }

  private DateTimeFormatter dateDayFormatter(){
    Locale local = new Locale("id", "ID");
    String pattern = "dd";
    return DateTimeFormatter.ofPattern(pattern, local);
  }
  private DateTimeFormatter dateFormatter(){
    Locale local = new Locale("id", "ID");
    String pattern = "dd MMMM yyyy";
    return DateTimeFormatter.ofPattern(pattern, local);
  }
}
