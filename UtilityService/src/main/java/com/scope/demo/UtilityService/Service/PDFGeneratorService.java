package com.scope.demo.UtilityService.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scope.demo.UtilityService.Utils.EmailClient;
import com.scope.demo.UtilityService.Utils.PdfGenerator;

@Service
public class PDFGeneratorService {

	@Autowired
	private PdfGenerator pdfGenerator;
	
	@Autowired
	private EmailClient emailGenerator;
	
	private File generatedPDFFile= new File("Quote.pdf");
	
	public void pdfGenerate(String name,String address,String phone,String email,String last_month_usage,String last_month_amount,String requested_term){
		
		String quote ="";
		Map<String,String> data = new HashMap<String,String>();
		SimpleDateFormat  simpleDate = new SimpleDateFormat("MM/dd/yyyy"); 
		Date date = new Date();
		data.put("date",simpleDate.format(date));
		data.put("name",name);
		data.put("address",address);
		data.put("phone", phone);
		data.put("last_month_usage", last_month_usage);
		data.put("last_month_amount", last_month_amount);
		data.put("requested_term",requested_term);
		/*logic to add quote amount*/
		if(requested_term.equals("6")){
			quote ="20";
		}
		else if(requested_term.equals("12")){
			quote ="18";
		}
		else if(requested_term.equals("24")){
			quote ="15";
         }
		else if(requested_term.equals("36")){
			quote ="12";
          }
		data.put("quote", quote);
		
		System.out.println("Quote is:" + quote);
		try {
			generatedPDFFile= pdfGenerator.createPdf("Content", data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*call to send email with generated PDF File*/
		
		try {
			emailGenerator.sendEMail(generatedPDFFile, email);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
