package com.teamddd.duckmap.service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SendMailService {
	private final RedisService redisService;
	@Value("${spring.mail.username}")
	private String fromEmail;
	@Value("${resetpassword.url}")
	private String resetPwUrl;
	@Autowired
	JavaMailSender mailSender;

	public String makeUuid() {
		return UUID.randomUUID().toString();
	}

	@Transactional
	public String sendEmailToUser(String email) {
		String uuid = makeUuid();
		String title = "요청하신 비밀번호 재설정 입니다."; // 이메일 제목
		String content = "대동덕지도" //html 형식으로 작성
			+ "<br><br>" + "아래 링크를 클릭하면 비밀번호 재설정 페이지로 이동합니다." + "<br>"
			+ "<a href=\"" + resetPwUrl + "/" + uuid + "\">"
			+ resetPwUrl + "/" + uuid + "</a>" + "<br><br>"
			+ "해당 링크는 24시간 동안만 유효합니다." + "<br>"; //이메일 내용 삽입
		mailSend(email, title, content);
		saveUuidAndEmail(uuid, email);
		return uuid;
	}

	//이메일 전송 메소드
	public void mailSend(String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		// true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다.
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(new InternetAddress(fromEmail, "대동덕지도"));
			helper.setTo(toMail);
			helper.setSubject(title);
			// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
			helper.setText(content, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

	// UUID와 Email을 Redis에 저장
	@Transactional
	public void saveUuidAndEmail(String uuid, String email) {
		long uuidValidTime = 60 * 60 * 24 * 1000L; // 24시간
		redisService.setValuesWithTimeout(uuid, // key
			email, // value
			uuidValidTime); // timeout(milliseconds)
	}
}
