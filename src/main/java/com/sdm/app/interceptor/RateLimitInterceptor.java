package com.sdm.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  private static final long MAX_REQUESTS = 100;
  private static final long TIME_WINDOW_MS = 2 * 60000;

  // Menggunakan Map untuk menyimpan jumlah request per IP
  private final Map<String, Long> requestCounts = new HashMap<>();
  private final Map<String, Long> timestamps = new HashMap<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String clientIp = request.getRemoteAddr();
    long now = System.currentTimeMillis();

    // Jika IP baru atau waktu window habis, reset counter untuk IP ini
    timestamps.putIfAbsent(clientIp, now);
    requestCounts.putIfAbsent(clientIp, 0L);

    if (now - timestamps.get(clientIp) > TIME_WINDOW_MS) {
      timestamps.put(clientIp, now);
      requestCounts.put(clientIp, 0L); // Reset counter untuk IP ini
    }

    long requestCount = requestCounts.get(clientIp);

    if (requestCount < MAX_REQUESTS) {
      requestCounts.put(clientIp, requestCount + 1);
      return true; // Lanjutkan request
    } else {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.getWriter().write("Too many requests from IP: " + clientIp);
      return false; // Blokir request
    }
  }
}

//@Component
//public class RateLimitInterceptor implements HandlerInterceptor {
//
//  // Variabel untuk batas jumlah request dan waktu reset
//  private static final long MAX_REQUESTS = 10; // Maksimum 10 request
//  private static final long TIME_WINDOW_MS = 60000; // 60 detik
//
//  // Variabel untuk menyimpan state
//  private long requests = 0; // Counter request
//  private long timestamp = System.currentTimeMillis(); // Waktu awal untuk window rate limiting
//
//  @Override
//  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//    long now = System.currentTimeMillis();
//
//    // Jika waktu window telah berlalu, reset counter dan timestamp
//    if (now - timestamp > TIME_WINDOW_MS) {
//      timestamp = now;
//      requests = 0; // Reset counter setiap 1 menit
//    }
//
//    // Periksa apakah jumlah request sudah melebihi batas
//    if (requests < MAX_REQUESTS) {
//      requests++; // Tambah counter request
//      return true; // Izinkan request untuk dilanjutkan ke controller
//    } else {
//      // Jika batas request terlampaui, blokir request dengan kode HTTP 429 (Too Many Requests)
//      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//      response.getWriter().write("Too many requests, please try again later");
//      return false; // Blokir request, tidak diteruskan ke controller
//    }
//  }
//}
