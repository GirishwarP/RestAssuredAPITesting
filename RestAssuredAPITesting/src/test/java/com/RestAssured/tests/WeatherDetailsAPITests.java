package com.RestAssured.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.DeserializedClassesFromJSON.WeatherDetails;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;

public class WeatherDetailsAPITests {
	
	@Test
	//test to get the weather details, using request() method
	public void getWeatherDetailsRequestMethod() {
		//specifying the base url of web service
		RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
		
		//getting RequestSpecification object of request the we want to send to server
		RequestSpecification httpRequest = RestAssured.given();
		
		//getting response given by the server
		//request method is used which accept method Type and the method URL as it's parameters
		Response response = httpRequest.request(Method.GET, "/pune");
		
		//getting the body of the response 
		String responseBody = response.getBody().asString();
		System.out.println("Response Body => " + responseBody);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200, "Correct status code returned");
		
		System.out.println("-------------------------------------");
	}
	
	@Test
	//test to get the weather details, using get() method
	public void getWeatherDetailsGetMethod() {
		RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city"; 
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/mumbai");
		String  responseBody = response.getBody().asString();
		System.out.println("Response Body => " + responseBody);
		
		//getting specific header
		String contentType = response.header("content-type");
		System.out.println("Content-type : " + contentType);
		
		//getting all headers
		Headers headers = response.headers();
		for(Header header : headers) {
			System.out.println(header.getName() + " : " + header.getValue());
		}
		
		//getting status line and its validation
		String statusLine = response.statusLine();
		System.out.println("Status Line => " + statusLine);
		Assert.assertEquals(statusLine, "HTTP/1.1 200 OK");
		
		// getting perticular header using JsonPath
		JsonPath jsonPath = response.jsonPath();
		String city = jsonPath.get("City");
		System.out.println("Retrive city is:"+city);
		
		System.out.println("-------------------------------------");
	}
	
	@Test
	//test to post registration details using post() method and JSONObject
	public void postDetailsTest() {
		RestAssured.baseURI = "http://restapi.demoqa.com/customer";
		RequestSpecification httpRequest = RestAssured.given();
		
		//Creating payload for post() method
		JSONObject payload = new JSONObject();
		
		payload.put("FirstName", "Girishwar");
		payload.put("LastName", "Patil");
		payload.put("UserName", "GPatil20135");
		payload.put("Password", "test@123");
		payload.put("Email", "girish.patil@gmail.com");
		
		//sending JSONObject payload as body to the httpRequest
		httpRequest.body(payload.toJSONString());
		
		//calling post() method
		Response response = httpRequest.post("/register");
		
		//getting status code of post request
		int statusCode = response.getStatusCode();
		System.out.println("Post Request status code => " + statusCode);
		
		System.out.println("-------------------------------------");
	}
	
	@Test
	//test to deserialize the JSON to simple POJO and getting details
	public void JSONBodyDeserialization() {
		RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/hyderabad");
		ResponseBody body = response.getBody();
		
		//Deserializing the response body into WeatherDetails
		WeatherDetails weatherDetails = body.as(WeatherDetails.class);
		
		//Getting the weather deatils
		System.out.println("Weather Details of Hyderabad : ");
		System.out.println("City : " + weatherDetails.City);
		System.out.println("Temperature : " + weatherDetails.Temperature);
		System.out.println("Humidity : " + weatherDetails.Humidity);
		System.out.println("WeatherDescription : " + weatherDetails.WeatherDescription);
		System.out.println("WindDirectionDegree : " + weatherDetails.WindDirectionDegree);
		System.out.println("WindSpeed : " + weatherDetails.WindSpeed);
		
		System.out.println("-------------------------------------");
	}
	
	@Test
	//Aunthentication test
	public void APIAunthenticationTest()
	{
		RestAssured.baseURI = "http://restapi.demoqa.com/authentication/CheckForAuthentication";
		RequestSpecification request = RestAssured.given();

		Response response = request.get();
		System.out.println("Status code: " + response.getStatusCode());
		System.out.println("Status message " + response.body().asString());
		
		System.out.println("-------------------------------------");
	}
	
	@Test
	public void JSONPathUsageTest() {
		RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("");
		JsonPath jsonPath = response.jsonPath();
		List<String> allCities = jsonPath.getList("WeatherDetails.City");
		System.out.println("No. of Books : " + allCities.size());
		
		System.out.println("-------------------------------------");
	} 
}
