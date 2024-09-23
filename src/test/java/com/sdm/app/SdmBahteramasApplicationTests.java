package com.sdm.app;

import com.sdm.app.enumrated.KopType;
import com.sdm.app.model.req.create.EmailRequest;
import com.sdm.app.service.impl.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class SdmBahteramasApplicationTests {

	private final EmailService service;

	@Autowired
	SdmBahteramasApplicationTests(EmailService service) {
		this.service = service;
	}

	@Test
	void contextLoads() {
	}


	@Test
	void testDate(){
		String pattern = "dd MMMM yyyy";

		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
	}

//	@Test
//	void sentEmail() {
//		EmailRequest request = new EmailRequest();
//		request.setName("Otong Surotong Markotong");
//		request.setNip("123456 789 0123 445");
//		request.setType(KopType.KARENA_ALASAN_PENTING);
//		request.setReason("Lorem ipsum dolor, sit amet consectetur adipisicing elit. Dolores voluptates iure amet libero placeat autem sit culpa nulla aliquam perspiciatis.");
//		request.setToken("atoken");
//
//		service.sendEmailHTMLFormat(request);
//	}
}
