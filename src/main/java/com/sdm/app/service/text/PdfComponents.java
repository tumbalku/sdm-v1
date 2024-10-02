package com.sdm.app.service.text;

import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.sdm.app.service.text.PdfUtils.*;
import static com.sdm.app.service.text.PdfUtils.image;

@Service
public class PdfComponents {

  public static final float CM_TO_PT = 28.35f;

  public static float cmToPt(float cm){
    return cm * CM_TO_PT;
  }
  public static Table headerLandscape() throws IOException {
    Table header = new Table(new float[]{
            10, 10
    });

    Table headerTitle = new Table(new float[]{10});

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
    Table socials = new Table(new float[]{10, 10, 10, 10});
    socials.addCell(setText("Email :", 9).setBold().setItalic());
    socials.addCell(link("mailto:admin@rsud-bahteramas.go.id", "admin@rsud-bahteramas.go.id ", 9).setPaddingLeft(-2f));
    socials.addCell(setText("Website :", 9).setBold().setItalic());
    socials.addCell(link("www.rsud-bahteramas.go.id", "www.rsud-bahteramas.go.id", 9).setPaddingLeft(-2f));
    socials.setHorizontalAlignment(HorizontalAlignment.CENTER);
    headerTitle.addCell(new Cell().add(socials).setBorder(Border.NO_BORDER));

    header.addCell(new Cell().add(image("logo.png")
                    .setWidth(60f)
                    .setHeight(56f))
            .setBorder(Border.NO_BORDER)
    );
    header.addCell(new Cell().add(headerTitle).setBorder(Border.NO_BORDER));
    header.setHorizontalAlignment(HorizontalAlignment.CENTER);
    return header;
  }
  public static Table headerPortrait() throws IOException {
    Table header = new Table(new float[]{
            percentPerWidth(container, 1.0f / 12),
            percentPerWidth(container, 11.0f / 12),
    });

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
    Table socials = new Table(new float[]{10, 10, 10, 10});
    socials.addCell(setText("Email :", 9).setBold().setItalic());
    socials.addCell(link("mailto:admin@rsud-bahteramas.go.id", "admin@rsud-bahteramas.go.id ", 9).setPaddingLeft(-2f));
    socials.addCell(setText("Website :", 9).setBold().setItalic());
    socials.addCell(link("www.rsud-bahteramas.go.id", "www.rsud-bahteramas.go.id", 9).setPaddingLeft(-2f));
    socials.setHorizontalAlignment(HorizontalAlignment.CENTER);
    headerTitle.addCell(new Cell().add(socials).setBorder(Border.NO_BORDER));

    header.addCell(new Cell().add(image("logo.png")
                    .setWidth(60f)
                    .setHeight(56f))
            .setBorder(Border.NO_BORDER)
    );
    header.addCell(new Cell().add(headerTitle).setBorder(Border.NO_BORDER));

    return header;
  }
}
