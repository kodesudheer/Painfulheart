package com.comcast.BulkSolve.Pojo;

import java.util.List;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Routes {
	
	/*@JsonProperty
	List <String> routes;*/

	@JsonProperty
	List<JSONObject> routesJson;
	public List<JSONObject> getRoutesJson() {
		return routesJson;
	}

	public void setRoutesJson(List<JSONObject> routesJson) {
		this.routesJson = routesJson;
	}

	/*public List<String> getRoutes() {
		return routes;
	}

	public void setRoutes(List<String> routes) {
		this.routes = routes;
	}*/
	
	

}
