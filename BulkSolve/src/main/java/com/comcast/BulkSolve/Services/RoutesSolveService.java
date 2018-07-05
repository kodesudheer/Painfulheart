package com.comcast.BulkSolve.Services;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.spatial.geometry.JGeometry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comcast.BulkSolve.Dao.DatabaseConnection;
import com.comcast.BulkSolve.Dao.SolveRoutesDao;
import com.comcast.BulkSolve.Exceptions.JsonParsingException;
import com.comcast.BulkSolve.Pojo.Routes;
import com.comcast.BulkSolve.Utils.JArrayToDoubleArrayConverter;

@Service
public class RoutesSolveService {

	@Autowired
	public SolveRoutesDao routesDao;
	
	/*Method to check whether the Response from the server is valid/error response*/
	public boolean checkResponse(String output){
		Pattern patern = Pattern.compile("appliedCostMatrix");
		Matcher matcher = patern.matcher(output);
		System.out.println("response length is:" + output.length() + output);
		if(matcher.find()){
			
            System.out.println("got the correct response from the server finally");
			
			  return true;
			
		}else{
			
			return false;
		}
		
	}
	
	public void processRoutesGeometry(String output,double latitude,double longitude,int distance,String solveType) throws Exception{
		
		JSONParser jParser = new JSONParser();
		int count=1;
		try {
			JSONObject jObject =(JSONObject)jParser.parse(output);
			JSONArray routes=(JSONArray)jObject.get("routes");
			Iterator itr = routes.iterator();
			
			while(itr.hasNext()){
				JSONObject innerJson =(JSONObject)itr.next();
				JSONArray options = (JSONArray)innerJson.get("routeOptions");
				Iterator itr1 = options.iterator();
				
				while(itr1.hasNext()){
					
					JSONObject resJson=(JSONObject)itr1.next();
					JSONObject otionAttr =(JSONObject)resJson.get("optionAttributes");
					JSONObject spatialRef =(JSONObject)otionAttr.get("spatialReference");
					JSONObject geometry =(JSONObject)spatialRef.get("geometry");
					JSONArray pathsArray =(JSONArray)geometry.get("paths");
					System.out.println("paths array is" + pathsArray);
					int [] eleinfo={1,2,1};
					double [] ordArray = null;
					if(pathsArray !=null){
						ordArray=JArrayToDoubleArrayConverter.pathsArray(pathsArray);
					}
					
					/*double [] ordArray = {-121.92963866199995,37.730608421000056,0,-121.92969499999998,37.730705000000057,1473.1024818164826,-121.92969499999998,37.730705000000057,2173.1024818164824,-121.9297269999999,37.730759000000035,2999.0671189006807};*/
					JGeometry jGeo = new JGeometry(2002,4326,eleinfo,ordArray);
					/*if(pathsArray != null){
						JGeometry jGeo = new JGeometry(2002,4326,eleinfo,pathsArray);
						String PathArrayStr = pathsArray.toString().replaceAll("\\[","").replaceAll("\\]","");
						System.out.println("PATHS ARRAY STring is:" + PathArrayStr);
						routesDao.saveSolvedRoutesGeometry(count, latitude, longitude, distance, solveType, PathArrayStr);	
					}
					count++;*/
					routesDao.saveSolvedRoutesGeometry1(count, latitude, longitude, distance, solveType, jGeo);	
					count=count+1;
					System.out.println("count is:" +count);
				}
				
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Routes getSolvedRoute(double latitude,double longitude,int distance,String solveType)throws JsonParsingException{
		
		int count=this.getRouteRecordCount(latitude, longitude, distance, solveType);
		if(count >=1){
		Routes routes =(Routes)routesDao.getSolvedRoutes(latitude, longitude, distance, solveType);
		return routes;
		}else{
			/* here write logic to get nearest neghbour for given lat longs*/
			Routes routes = (Routes)routesDao.getNearestSolvedRoutes(latitude, longitude, solveType);
			
			return routes;
		}
	}
	
	public int getRouteRecordCount(double latitude,double longitude,int distance,String solveType){
		
		int count=routesDao.getRecordCount(latitude, longitude, distance, solveType);
		return count;
	}
	
	public int saveSolvedRoutes(double latitude,double longitude,int distance,int regionCostMatrix,String solveType,String sortby,String jobcost,String hfcType,int routecount,String isIsloatedNetwork,String locaBoxSolve,String environment,String output)throws Exception{
		
		int count =0;
		
		if(this.checkResponse(output)){
			
			count=routesDao.saveSolvedRoutes(latitude, longitude, distance, regionCostMatrix, solveType, sortby, jobcost, hfcType, routecount,isIsloatedNetwork,locaBoxSolve,environment,output);
			if(count > 0){
				
				processRoutesGeometry(output,latitude,longitude,distance,solveType);
				/*Call method to store the paths geometry to routes table*/
				
			}
			
		}
		else{
			System.out.println("in else block of service method" + latitude + longitude);
			
			count = routesDao.saveSolvedRoutesErrors(latitude, longitude, distance, regionCostMatrix, solveType, sortby, jobcost, hfcType, routecount, output);
			
		}
		return count;
	}
}
