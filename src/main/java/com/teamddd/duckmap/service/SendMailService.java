package com.teamddd.duckmap.service;

import java.util.Random;

import javax.mail.MessagingException;
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

	@Value("${spring.mail.username}")
	private String fromEmail;
	@Autowired
	JavaMailSender mailSender;

	public int makeRandomNumber() {
		Random random = new Random();
		return random.nextInt(999999); // 랜덤 6자리 난수설정
	}

	public String sendVerification(String email) {
		int verifyCode = makeRandomNumber();
		String setFrom = fromEmail;
		String toMail = email;
		String title = "비밀번호 찾기 인증번호 입니다."; // 이메일 제목
		String content = "대동덕지도" //html 형식으로 작성
			+ "<br><br>" + "인증 번호는 " + verifyCode + "입니다." + "<br>"
			+ "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
		mailSend(setFrom, toMail, title, content);
		return Integer.toString(verifyCode);
	}

	//이메일 전송 메소드
	public void mailSend(String setFrom, String toMail, String title, String content) {
		MimeMessage message = mailSender.createMimeMessage();
		// true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능하다.
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
			helper.setText(content, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}
