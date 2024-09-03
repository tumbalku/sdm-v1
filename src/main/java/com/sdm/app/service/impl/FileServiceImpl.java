package com.sdm.app.service.impl;

import com.sdm.app.entity.File;
import com.sdm.app.entity.Letter;
import com.sdm.app.model.res.FileResponse;
import com.sdm.app.repository.FileRepository;
import com.sdm.app.utils.FileHelper;
import com.sdm.app.utils.GeneralHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;

@Service
@AllArgsConstructor
public class FileServiceImpl {

  private final FileRepository repository;

  @Transactional
  public FileResponse saveFile(MultipartFile document){
    ByteArrayOutputStream outputStream = FileHelper.compressDocument(document);

    File file = new File();
    String URL = GeneralHelper.idNameConversion(document);
    file.setName(document.getOriginalFilename());
    file.setType(document.getContentType());
    file.setPath(URL);
    file.setData(outputStream.toByteArray());

    repository.save(file);

    FileResponse response = new FileResponse();
    response.setURL(URL);
    response.setSize(outputStream.size());
    response.setContentType(document.getContentType());

    return response;
  }


  @Transactional
  public String saveImage(MultipartFile image){
    ByteArrayOutputStream outputStream = FileHelper.compressImage(image);

    File file = new File();
    String URL = GeneralHelper.idNameConversion(image);
    file.setName(image.getName());
    file.setType(image.getContentType());
    file.setPath(URL);
    file.setData(outputStream.toByteArray());

    repository.save(file);
    return URL;
  }

  public byte[] getFile(String path) {
    File image = findByPath(path);
    return FileHelper.decompress(image.getData());
  }
  public File findByPath(String path){
    return repository.findByPath(path)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
  }

  @Transactional
  public void removePrevFile(String path) {
    findByPath(path);
    repository.deleteByPath(path);
  }
}
