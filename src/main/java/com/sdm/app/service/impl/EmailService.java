package com.sdm.app.service.impl;

import com.sdm.app.model.req.create.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@Service
public class EmailService {

  private static final String fe_url = "https://arubasdm.online";
  public static final String UTF_8_ENCODING = "UTF-8";
  private final TemplateEngine templateEngine;
  private final JavaMailSender sender;

  public static String decisionUrl(String base, String token){
    return base + "/cuti/decision/" + token;
  }

  @Async
  public void sendEmailHTMLFormat(EmailRequest request){
    try {
      Context context = new Context();
      context.setVariable("name", request.getName());
      context.setVariable("nip", request.getNip());
      context.setVariable("reason", request.getReason());
      context.setVariable("url", decisionUrl(fe_url, request.getToken()));
      context.setVariable("type", request.getType().getDescription());

      String text = templateEngine.process("index", context);


      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
      String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

      helper.setPriority(1);
      helper.setSubject(request.getName() + " Mengajukan Cuti: " + request.getType().getDescription() + " " + timeStamp);
      helper.setTo("muhammadarsilalhabsy@gmail.com");
      helper.setText(text, true);

      sender.send(message);
      System.out.println("success");
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }
}
