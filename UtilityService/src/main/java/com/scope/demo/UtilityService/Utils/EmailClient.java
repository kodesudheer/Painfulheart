package com.scope.demo.UtilityService.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailClient {

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private PdfGenerator pdfGenerator;
	
	@Autowired
	private Environment environment;

	
	File file = null;;
	public void sendMail(String subject, String body, String to) throws IOException, MessagingException{
		Map<String,String> data = new HashMap<String,String>();
		data.put("name", "Vishnu");
		try {
			file=pdfGenerator.createPdf("Content", data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setTo(to);
		helper.setFrom("");
		helper.setText(body);
		helper.setSubject(subject);
		helper.addAttachment("Your Receipt.pdf",file);
		sender.send(message);
		
		}
	
	/*to send email with formatted PDF file*/
	
	public void sendEMail(File file,String email) throws IOException, MessagingException{
		
		System.out.println("From field is:" +environment.getProperty("email.from"));
		String subject = "Your new Quote for Your usage term";
		String body = "Based upon your selected term of usage attached pdf has your Quote";
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setTo(email);
		helper.setFrom(environment.getProperty("email.from"));
		helper.setText(body);
		helper.setSubject(subject);
		helper.addAttachment("YourQuote.pdf",file);
		sender.send(message);
		
		}
	
	
	public static void main(String [] args){
		EmailClient ec = new EmailClient();
		try {
			ec.sendMail("Testemail","Hello vishnu","vishnuvikranth@gmail.com");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}