package com.comcast.BulkSolve.Utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.json.simple.JSONArray;

public class JArrayToDoubleArrayConverter {
	
	public static double[] pathsArray(JSONArray jArray){
		
		String PathStr = jArray.toString().replaceAll("\\[","").replaceAll("\\]","");
		
		String [] pathArray = PathStr.split(",");
		List<Double>list= new ArrayList<Double>();
		for(String path : pathArray){
			double db = Double.parseDouble(path);
			list.add(db);
		}
		Double [] dbArray = new Double[list.size()];
		list.toArray(dbArray);
		double[] pathResults=ArrayUtils.toPrimitive(dbArray);
		return pathResults;
	}

}
