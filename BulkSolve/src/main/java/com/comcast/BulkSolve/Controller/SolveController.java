package com.comcast.BulkSolve.Controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.comcast.BulkSolve.Exceptions.JsonParsingException;
import com.comcast.BulkSolve.Pojo.Routes;
import com.comcast.BulkSolve.Services.RoutesSolveService;


@RestController
@EnableAutoConfiguration
public class SolveController {

	@Autowired
	public RoutesSolveService routeService;
	/*This is the method to fetch DB records for Transaction mode*/
	@RequestMapping(value="/getSolveRoutes",method=RequestMethod.GET,produces="application/json")
	public Object getSolveResult(HttpServletRequest request,HttpServletResponse response) {
		
		if((request.getParameter("latitude")== null ||request.getParameter("latitude")=="")  || (request.getParameter("longitude")== null ||request.getParameter("longitude")=="")|| (request.getParameter("distance")==null || request.getParameter("distance")=="") || (request.getParameter("solveType")==null || request.getParameter("solveType")=="") ){
			
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
			
			
		}

		double latitute = Double.parseDouble(request.getParameter("latitude"));
		double longitude =Double.parseDouble(request.getParameter("longitude"));
		int distance =Integer.parseInt(request.getParameter("distance"));
		String solveType=request.getParameter("solveType");
		Routes routes = null;
		try{
		 routes = (Routes)routeService.getSolvedRoute(latitute, longitude, distance, solveType);
		}catch(JsonParsingException ex){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return routes;
		
		
	}
	/*This is the method to check record exist or not in DB for Bulk mode*/
	@RequestMapping(value="/getSolveRoutesForBulk",method=RequestMethod.GET,produces="application/json")
	public Object getSolveResultForBulk(HttpServletRequest request,HttpServletResponse response){
		
      if((request.getParameter("latitude")== null ||request.getParameter("latitude")=="")  || (request.getParameter("longitude")== null ||request.getParameter("longitude")=="")|| (request.getParameter("distance")==null || request.getParameter("distance")=="") || (request.getParameter("solveType")==null || request.getParameter("solveType")=="") ){
			
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
			
			
		}

		double latitute = Double.parseDouble(request.getParameter("latitude"));
		double longitude =Double.parseDouble(request.getParameter("longitude"));
		int distance =Integer.parseInt(request.getParameter("distance"));
		String solveType=request.getParameter("solveType");
		
		int count = routeService.getRouteRecordCount(latitute, longitude, distance, solveType);
		if(count >= 1){
		
		return new String("exist");
		}else{
			String responsenvl = new String("NotExist");
			System.out.println("in else block");
			return responsenvl;
		}
	}
	
	@RequestMapping(value="/putSolvedRoutes",method=RequestMethod.POST,produces="application/json")
	public int insertRouteResponse(HttpServletRequest request,@RequestBody JSONObject routeResponse,HttpServletResponse response){
		double latitute =0.0;
		double longitude=0.0;
		int distance=0;
		String solveType="";
		int regionCostMetrix=0;
		String sortBy="";
		String jobCost="";
		String hfcType="";
		int routeCount=0;
		String isIsloatedNetwork="false";
		String locaBoxSolve ="N";
		String environment ="Dev";
		
		String outputResponse="";
		int count =0;
		try{
			if(request.getParameter("latitude")!=null)
			latitute = Double.parseDouble(request.getParameter("latitude"));
			if(request.getParameter("longitude")!=null)
		    longitude =Double.parseDouble(request.getParameter("longitude"));
			if(request.getParameter("distance")!=null)
			distance =Integer.parseInt(request.getParameter("distance"));
			if(request.getParameter("solveType")!=null)
			 solveType=request.getParameter("solveType");
			if(request.getParameter("rgncostmetrix")!=null)
			 regionCostMetrix = Integer.parseInt(request.getParameter("rgncostmetrix"));
			if(request.getParameter("sortBy")!=null)
			 sortBy = request.getParameter("sortBy");
			if(request.getParameter("jobCost")!=null)
			 jobCost = request.getParameter("jobCost");
			if(request.getParameter("hfcType")!=null)
			 hfcType =request.getParameter("hfcType");
			if(request.getParameter("routeCount")!=null)
			 routeCount = Integer.parseInt(request.getParameter("routeCount"));
			if(request.getParameter("isIsloatedNetwork")!=null)
				isIsloatedNetwork = request.getParameter("isIsloatedNetwork");
			if(request.getParameter("locaBoxSolve")!=null)
				locaBoxSolve = request.getParameter("locaBoxSolve");
			if(request.getParameter("environment")!=null)
				environment = request.getParameter("environment");
			/*if(request.getParameter("output")!=null)
			 outputResponse= request.getParameter("output");*/
			outputResponse = routeResponse.toString();
			System.out.println("The json request is:" + outputResponse);
			 count = routeService.saveSolvedRoutes(latitute, longitude, distance, regionCostMetrix, solveType, sortBy, jobCost, hfcType, routeCount,isIsloatedNetwork,locaBoxSolve,environment,outputResponse);
		
		}catch(SQLException e){
			e.printStackTrace();
			/*here we have to throw proper error code if the parameter values are missed whne calling service*/
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
}
