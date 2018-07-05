package com.comcast.BulkSolve.Dao;

import java.util.ArrayList;
import java.util.List;

import oracle.spatial.geometry.JGeometry;
/*import oracle.sql.STRUCT;*/

import oracle.sql.STRUCT;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.comcast.BulkSolve.Exceptions.JsonParsingException;
import com.comcast.BulkSolve.Pojo.Routes;

@Repository
public class SolveRoutesDao extends DatabaseConnection{

	
	/*@Autowired
	public DatabaseConnection DbConnection;*/
	
	private static String SOLVED_ROUTES= "select nsx_ntwk_rte_resp from COE_NSX_LATLNG_DETAILS where nsx_lat = ? and nsx_lng= ? and nsx_slv_dist= ? and slv_type= ?";
	
	private static String RECORD_COUNT = "Select count(*) from COE_NSX_LATLNG_DETAILS where nsx_lat = ? and nsx_lng=? and nsx_slv_dist =? and nsx_slv_type= ?";
	
	private static String SAVE_SOLEVED_ROUTES="insert into COE_NSX_LATLNG_DETAILS " +
	                                                   "(REC_ID,NSX_LAT,NSX_LNG,NSX_SLV_DIST,NSX_REGN_COSTMTRX,NSX_SLV_TYPE,NSX_SORTBY,NSX_JOBCOST,NSX_HFC_TYPE,NSX_RTE_CNT,SAVED_DT,NSX_NTWK_RTE_RESP,nsx_isisloated_ntwrk,nsx_localbox_slv,nsx_env,SHAPE)" +
			                                           "values(NSX_LATLNG_Rec_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,MDSYS.SDO_GEOMETRY(2001,4326,MDSYS.SDO_POINT_TYPE(?,?,NULL),NULL,NULL))";
	
	private static String SAVE_SOLVED_ROUTE_ERR="insert into COE_NSX_LATLNG_ERR " +
	                                            "(REC_ID,LAT,LNG,DIST,REGN_COSTMTRX,SLV_TYP,SORTBY,JOBCOST,HFC_TYPE,RTE_CNT,SAVED_DT,NTWK_RTE_ERR)" +
	                                            "values(NSX_LATLNG_Err_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)";
	private static String NEAREST_SOLVED_ROUTES = "select nsx.ntwk_rte_resp from COE_NSX_LATLNG_DETAILS nsx where SDO_NN(nsx.shape,SDO_GEOMETRY(2002,4326,NULL,SDO_ELEM_INFO_ARRAY(1,2,1),SDO_ORDINATE_ARRAY(?,?)),'SDO_BATCH_SIZE=10 distance=20',1)='TRUE' and nsx.slv_typ=?";
	
	private static String SAVE_ROUTES_GEOMETRY ="insert into COE_NSX_ROUTES(rec_id,Route_Num,Lat,lng,dist,slv_typ,shape)values(NSX_LATLNG_Rec_ID.CURRVAL,?,?,?,?,?,MDSYS.SDO_GEOMETRY(2002,4326,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),MDSYS.SDO_ORDINATE_ARRAY(?)))";
	
	private static String SAVE_ROUTES_GEOMETRY1 ="insert into COE_NSX_ROUTES(rec_id,Route_Num,Lat,lng,dist,slv_typ,shape)values(NSX_LATLNG_Rec_ID.CURRVAL,?,?,?,?,?,?)";
	
	public int getRecordCount(double latitude,double longitude,int distance,String solveType){
		
		int count =0;
		count=getJdbcTemplate().queryForObject(RECORD_COUNT,new Object[]{latitude,longitude,distance,solveType},Integer.class);
		
		return count;
	}
	
	public Object getSolvedRoutes(double latitude,double longitude,int distance,String solveType)throws JsonParsingException {
		
		/*int count = this.getRecordCount(latitude, longitude, distance, solveType);*/
		JSONParser jParser = new JSONParser();
		List<String> routesList = null;
		List<JSONObject> routesListJson =null;
		Routes routesPojo = null;
		try{
			routesList= getJdbcTemplate().queryForList(SOLVED_ROUTES,new Object[]{latitude,longitude,distance,solveType}, String.class);
			routesListJson = new ArrayList();
		for(String Route: routesList){
			System.out.println("Rout is:" + Route);
			JSONObject jobj=null;
			try{
		    jobj =(JSONObject)jParser.parse(Route);
			}catch(Exception e){
				throw new JsonParsingException("Error parsing String to JSON");
			}
		routesListJson.add(jobj);
		}
		routesPojo = new Routes();
//		routesPojo.setRoutes(routesList);
		routesPojo.setRoutesJson(routesListJson);
		}catch(Exception e){
			e.printStackTrace();
		}
		return routesPojo;
		
	}
	
	/*This method is to get list of json routes which are near to given lat,longs*/
	public Object getNearestSolvedRoutes(double latitude,double longitude,String solveType)throws JsonParsingException{
		
		JSONParser jParser = new JSONParser();
		List<String> routesList = null;
		List<JSONObject> routesListJson =null;
		Routes routesPojo = null;
		try{
			routesList= getJdbcTemplate().queryForList(NEAREST_SOLVED_ROUTES,new Object[]{longitude,latitude,solveType}, String.class);
			routesListJson = new ArrayList();
		for(String Route: routesList){
			System.out.println("Rout is:" + Route);
			JSONObject jobj=null;
			try{
		    jobj =(JSONObject)jParser.parse(Route);
			}catch(Exception e){
				throw new JsonParsingException("Error parsing String to JSON");
			}
		routesListJson.add(jobj);
		}
		routesPojo = new Routes();
//		routesPojo.setRoutes(routesList);
		routesPojo.setRoutesJson(routesListJson);
		}catch(Exception e){
			e.printStackTrace();
		}
		return routesPojo;
		
	}
	
	public int saveSolvedRoutes(double latitude,double longitude,int distance,int regionCostMatrix,String solveType,String sortby,String jobcost,String hfcType,int routecount,String isIsloatedNetwork,String locaBoxSolve,String environment,String output)throws Exception{
		
		
		int count = getJdbcTemplate().update(SAVE_SOLEVED_ROUTES, new Object[]{latitude,longitude,distance,regionCostMatrix,solveType,sortby,jobcost,hfcType,routecount,java.sql.Date.valueOf(java.time.LocalDate.now()),output,isIsloatedNetwork,locaBoxSolve,environment,longitude,latitude});
		
		return count;
	}
	
public int saveSolvedRoutesGeometry(int routeNumber,double latitude,double longitude,int distance,String solveType,String pathsArray)throws Exception{
		
		
		int count = getJdbcTemplate().update(SAVE_ROUTES_GEOMETRY, new Object[]{routeNumber,latitude,longitude,distance,solveType,pathsArray});
		
		return count;
	}
	
	public int saveSolvedRoutesErrors(double latitude,double longitude,int distance,int regionCostMatrix,String solveType,String sortby,String jobcost,String hfcType,int routecount,String error)throws Exception{
	
         int count = getJdbcTemplate().update(SAVE_SOLVED_ROUTE_ERR, new Object[]{latitude,longitude,distance,regionCostMatrix,solveType,sortby,jobcost,hfcType,routecount,java.sql.Date.valueOf(java.time.LocalDate.now()),error});
		
		return count;
	
	}
	
public int saveSolvedRoutesGeometry1(int routeNumber,double latitude,double longitude,int distance,String solveType,JGeometry pathsArray)throws Exception{
		
	   STRUCT obj = JGeometry.store(pathsArray, getJdbcConnection());
		
		int count = getJdbcTemplate().update(SAVE_ROUTES_GEOMETRY1, new Object[]{routeNumber,latitude,longitude,distance,solveType,obj});
		
		return count;
	}
}
