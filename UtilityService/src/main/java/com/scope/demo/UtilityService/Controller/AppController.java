package com.scope.demo.UtilityService.Controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scope.demo.UtilityService.Service.PDFGeneratorService;
import com.scope.demo.UtilityService.Utils.EmailClient;
@CrossOrigin(origins = "*")
@EnableAutoConfiguration
@RestController
public class AppController {

	@Autowired
	private EmailClient emailClient;
	@Autowired
	private PDFGeneratorService PdfGeneratorService;
	@RequestMapping(value="/sendemail",method=RequestMethod.POST,produces="text/plain")
	public void sendEmail(HttpServletRequest request){
		String name="";
		String address="";
		String Phone="";
		String email="";
		String last_month_usage="";
		String last_month_amount ="";
		String requested_term="";
		
		/*String subject =request.getParameter("subject");
		String body =request.getParameter("body");
		String to=request.getParameter("to");*/
		
		if(request.getParameter("name")!=null){
			name= request.getParameter("name");
		}
		if(request.getParameter("address")!=null){
			address= request.getParameter("address");
		}
		if(request.getParameter("Phone")!=null){
			Phone = request.getParameter("Phone");
		}
		if(request.getParameter("email")!=null){
			email = request.getParameter("email");
		}
		if(request.getParameter("last_month_usage")!=null){
			last_month_usage = request.getParameter("last_month_usage");
		}
		if(request.getParameter("last_month_amount")!=null){
			last_month_amount = request.getParameter("last_month_amount");
		}
		if(request.getParameter("requested_term")!=null){
			requested_term = request.getParameter("requested_term");
		}
		System.out.println("Requested term is:" + requested_term);
		PdfGeneratorService.pdfGenerate(name, address, Phone, email, last_month_usage, last_month_amount, requested_term);
		/*try {
			emailClient.sendMail(subject, body, to);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
}
