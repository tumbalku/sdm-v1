package com.sdm.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class SdmBahteramasApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void testDate(){
		String pattern = "dd MMMM yyyy";

		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
	}

}
