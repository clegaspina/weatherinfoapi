package com.weatherinfo.weatherinfoapi.controller;

import com.weatherinfo.weatherinfoapi.config.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {

	private static final String CITY_ID_1 = "2643743", //LONDON
								CITY_ID_2 = "3067696", //PRAGUE
								CITY_ID_3 = "5391959"; //SAN FRANCISCO
	
	String URL = "https://api.openweathermap.org/data/2.5/group?id=" + CITY_ID_1 + "," + 
								CITY_ID_2 + "," + CITY_ID_3 + "&APPID=" + 
								Configuration.getAPIKey() + "&units=metric";
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/weatherinfo/")
	public @ResponseBody Object getWeather() {
		List<Map<String,String>> responseList = new ArrayList<Map<String,String>>();
        Map<String,String> listObject;
        
        RestTemplate restTemplate = new RestTemplate();
        
        String response = restTemplate.getForObject(URL, String.class);
        
        try {       	
        	// Convert REST response into JSONObject to get JSONArray
	        JSONObject jsonObject = new JSONObject(response);
	        JSONArray jsonList = jsonObject.getJSONArray("list");	        
	        
	        // Iterate through the JSON array to get weather information
	        for(int i=0; i<jsonList.length(); i++) {
	        	JSONObject currObject = jsonList.getJSONObject(i);
	        	JSONObject weather = currObject.getJSONArray("weather").getJSONObject(0);
	        	String weatherDesc = weather.getString("description");
	        	String locationName = currObject.getString("name");
	        	String temperature = (currObject.getJSONObject("main").getDouble("temp"))+"";
	        	String country = currObject.getJSONObject("sys").getString("country");
	        	
	        	listObject = new LinkedHashMap<String,String>();
	        	
	        	// Store weather information in a HashMap
	        	listObject.put("location", locationName+", "+country);
	        	listObject.put("weather", weatherDesc);
		        listObject.put("temp", temperature);
		        
		        responseList.add(listObject);
	        }	        
	        			
        } catch(JSONException jsone) {
        	jsone.printStackTrace();
        }
        
        return responseList;
	}
}