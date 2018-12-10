package com.nba.baller.getyourring.controllers;

import com.nba.baller.getyourring.helpers.MimeTypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

@Slf4j
@RestController
@RequestMapping("/")
public class StaticController {

	@GetMapping("/static/**")
	public void getStaticContent(HttpServletRequest request, HttpServletResponse response) {

		String uri = "." + request.getRequestURI();

		ClassLoader cl = this.getClass().getClassLoader();
		URL url = cl.getResource(uri);

		if (url == null) {
			try {
				send404(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				sendFile(response, url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void send404(HttpServletResponse response) throws IOException {

		response.getWriter().write("File not found(404)");
	}

	private void sendFile(HttpServletResponse response,URL url) throws IOException {

		File file = new File(url.getFile());

		MimeTypeResolver mtr = new MimeTypeResolver(file);

		String mimeType = mtr.getMimeType();

		response.setContentType(mimeType);

		OutputStream os = response.getOutputStream();

		FileInputStream fileInputStream = new FileInputStream(file);

		int value;
		while ((value = fileInputStream.read()) != -1) {
			os.write(value);
		}
		fileInputStream.close();
	}

}
