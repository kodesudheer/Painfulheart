package com.scope.demo.UtilityService.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGenerator {
	@Autowired
	private TemplateEngine templateEngine;
	
	public File createPdf(String templateName, Map<String,String> map) throws Exception {
		
		Context ctx = new Context();
		
		if (map != null) {
		     Iterator itMap = map.entrySet().iterator();
		       while (itMap.hasNext()) {
			  Map.Entry pair = (Map.Entry) itMap.next();
		          ctx.setVariable(pair.getKey().toString(), pair.getValue());
			}
		}
		String processedHtml = templateEngine.process(templateName, ctx);
		String filename = "ElectricityQuote";
		 FileOutputStream os = null;
		 final File outputFile = File.createTempFile(filename,".pdf");
		try{
			/*final File outputFile = File.createTempFile(filename,".pdf");*/
			os = new FileOutputStream(outputFile);
			 ITextRenderer renderer = new ITextRenderer();
			 renderer.setDocumentFromString(processedHtml);
			 renderer.layout();
			 renderer.createPDF(os, false);
			 renderer.finishPDF();
			 System.out.println("PDF Created sucessfully");
			 os.close();
			 
			 
		}catch(Exception e){
			os.close();
			e.printStackTrace();
		}
		return outputFile;
		
	}
	
}
