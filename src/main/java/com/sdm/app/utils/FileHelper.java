package com.sdm.app.utils;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class FileHelper {

  private static final List<String> ALLOWED_DOCUMENT_CONTENT_TYPES = Arrays.asList(
          "application/pdf", // .pdf
          "application/msword", // .doc
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
          "application/vnd.ms-excel", // .xls
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
          "application/vnd.ms-powerpoint", // .ppt
          "application/vnd.openxmlformats-officedocument.presentationml.presentation" // .pptx
  );

  private static final List<String> ALLOWED_IMAGE_CONTENT_TYPES = Arrays.asList(
          "image/png",
          "image/jpeg",
          "image/jpg"
  );

  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

  private static void checkDocument(String contentType){
    if(!ALLOWED_DOCUMENT_CONTENT_TYPES.contains(contentType)){
      throw new ResponseStatusException(
              HttpStatus.UNSUPPORTED_MEDIA_TYPE,
              "Only allows file with extension (.pdf, .doc, .docx, .xls, .xls, .ppt, .pptx)");
    }
  }

  private static void checkImage(String contentType){
    if(!ALLOWED_IMAGE_CONTENT_TYPES.contains(contentType)){
      throw new ResponseStatusException(
              HttpStatus.UNSUPPORTED_MEDIA_TYPE,
              "Only allows image with extension (.png, .jpeg, .jpg)");
    }
  }

  private static void checkSize(Long size){
    if(size > MAX_FILE_SIZE){
      throw new ResponseStatusException(
              HttpStatus.PAYLOAD_TOO_LARGE,
              "Max file size should be 5MB");
    }
  }
  public static ByteArrayOutputStream compressDocument(MultipartFile file){
    String contentType = file.getContentType();
    checkDocument(contentType);
    checkSize(file.getSize());

    // Compress file data
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream)) {
      gzipOutputStream.write(file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return byteArrayOutputStream;
  }

  public static ByteArrayOutputStream compressImage(MultipartFile file){
    String contentType = file.getContentType();
    checkImage(contentType);
    checkSize(file.getSize());

    // Compress file data
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream)) {
      gzipOutputStream.write(file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return byteArrayOutputStream;
  }

  public static byte[] decompress(byte[] compressedData) {
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
         GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(byteArrayInputStream);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      byte[] buffer = new byte[1024];
      int length;
      while ((length = gzipInputStream.read(buffer)) > 0) {
        byteArrayOutputStream.write(buffer, 0, length);
      }
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Error while decompressing image", e);
    }
  }

  private static final Map<String, String> mediaTypeToExtensionMap = new HashMap<>();

  static {
    mediaTypeToExtensionMap.put("application/msword", ".doc");
    mediaTypeToExtensionMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
    mediaTypeToExtensionMap.put("application/pdf", ".pdf");
    mediaTypeToExtensionMap.put("image/jpeg", ".jpg");
    mediaTypeToExtensionMap.put("image/png", ".png");
    mediaTypeToExtensionMap.put("application/vnd.ms-powerpoint", ".ppt");
    mediaTypeToExtensionMap.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx");
    // Tambahkan tipe media lainnya sesuai kebutuhan
  }

  public static String getExtensionFromMediaType(String mediaType) {
    return mediaTypeToExtensionMap.getOrDefault(mediaType, "");
  }

}
