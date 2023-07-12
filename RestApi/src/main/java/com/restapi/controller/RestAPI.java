package com.restapi.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.restapi.exception.Exceptio;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.restapi.entity.RestEntity;
import com.restapi.entity.UrlContainer;
import com.restapi.pojo.ClientPojo;
import com.restapi.pojo.EmployeePojo;
import com.restapi.repository.RestRepo;
import com.restapi.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;  
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RestAPI {

	@Autowired
	private RestRepo restRepo;
	
	@Autowired
	private UrlContainer urlContainer;
	
	@Autowired
	private EmployeeService employeeService;
	
//	@RequestMapping("/")
//	public String index(){
//		return "index.html"; 
//	}
	
	
	@GetMapping("/date")
	@ResponseBody
	public String getTime() {
		Date date=new Date();
		
		return String.valueOf(date);
		
	}
	@GetMapping("/emp")
	public String viewEmployeeDetails(Model model) {
		model.addAttribute("EmployeeDetails", employeeService.getAll());
		return "employee";
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/employ")
	@ResponseBody
	public List<RestEntity> getAllemp(){
		return employeeService.getAll();
	}
	@GetMapping("/addEmployee")
	public String addEmployee(Model model) {
		RestEntity restEntity=new RestEntity();
		model.addAttribute("employee", restEntity);
		return "new_employee";
	}
	
	@PostMapping("/saveEmployee")
	public String saveEmploye(@ModelAttribute("employee") RestEntity restEntity) {
		employeeService.newEmp(restEntity);
		return "redirect:/emp";
	}
	
	@GetMapping("/showUpdateForm/{id}")
	public String showUpdateForm(@PathVariable (value="id") long id,Model model) {
		RestEntity restEntity=employeeService.getById(id);
		model.addAttribute("employee", restEntity);
		return "update_employee";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value ="id") long id) {
		this.employeeService.delEmp(id);
		return "redirect:/emp";
	}
	
	
	
	
	
	
//===========================================================================================================//
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(path = "/employ")
	@ResponseBody
	public RestEntity saveEmployee(@RequestBody EmployeePojo ojo) {
		
		System.out.println("Checking In HTML..."+ojo);
		RestEntity restEntity = new RestEntity();
		restEntity.setAge(ojo.getAge());
		restEntity.setName(ojo.getName());
		restEntity.setSalary(ojo.getSalary());
	//	restEntity.setCreatedtime(current_timestamp);
		
		RestEntity rest=restRepo.save(restEntity);
		
		return rest;
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("/employ/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteById(@PathVariable Long id){
		try {
			System.out.println("waiting");
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RestEntity restEntity=restRepo.findById(id)
				.orElseThrow(() -> new Exceptio("Employee not exist with id :" + id));
		restRepo.delete(restEntity);
		Map<String, Boolean> response = new HashMap<>();
		
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping(path = "/employ/{id}", consumes = {"application/json"})
	@ResponseBody
	public RestEntity updateEmployee(@PathVariable Long id, @RequestBody EmployeePojo ojo) {
		
		//RestEntity restEntity = new RestEntity();
		EmployeePojo pojo=new EmployeePojo();
		
		RestEntity restEntity=restRepo.findById(id)
				.orElseThrow(() -> new Exceptio("Employee not exist with id :" + id));
		
		restEntity.setAge(ojo.getAge());
		restEntity.setName(ojo.getName());
		restEntity.setSalary(ojo.getSalary());
		RestEntity rest=restRepo.save(restEntity);
		return rest;
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/employ/{id}")
	@ResponseBody
	public ResponseEntity<Optional<RestEntity>> getEmployeeById(@PathVariable Long id) {
		
		Optional<RestEntity> restEntity=restRepo.findById(id);
		
		return ResponseEntity.ok(restEntity);
		
			
	}
	
	@ResponseBody
	@PostMapping("/clientDetails")
	public ResponseEntity<String> clientListDetails(
	        @RequestParam("BRANCH_CODE") String branchCode,
	        @RequestParam("MOBILE_NO") String mobileNo,
	        @RequestParam("EMAIL_ID") String emailId,
	        @RequestParam("CLIENT_ID") String clientId) throws JsonProcessingException {

	    // Create the request body as a JSON string
	    ObjectMapper objectMapper = new ObjectMapper();
	    String requestBodyJson = objectMapper.writeValueAsString(Map.of(
	            "BRANCH_CODE", branchCode,
	            "MOBILE_NO", mobileNo,
	            "EMAIL_ID", emailId,
	            "CLIENT_ID", clientId
	    ));

	    System.out.println("brcode is"+branchCode);
	    System.out.println("mobNo is"+mobileNo);
	    System.out.println("emailcode is"+emailId);
	    System.out.println("cId is is"+clientId);
	    // Set the headers and create the HttpEntity
	    HttpHeaders headers = new HttpHeaders();
//	    headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);
	    HttpEntity<String> request = new HttpEntity<>(requestBodyJson, headers);

	    System.out.println("Request is is"+request);
	    System.out.println("URL is is"+urlContainer.getClientDetailsUrl());
	    
	    RestTemplate restTemplate= new RestTemplate();
	    // Send the request and get the response
	    ResponseEntity<String> response = restTemplate.postForEntity(
	            urlContainer.getClientDetailsUrl(),
	            request,
	            String.class
	    );

	    System.out.println("OutPut is  "+response.getBody());
	    return ResponseEntity.ok(response.getBody());
	}

		/* 
		  @ResponseBody
@PostMapping("/clientDetails")
public ResponseEntity<String> ClientListDetails(@RequestBody ClientPojo requestBody, @CookieValue("JSESSIONID") String sessionId) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(urlContainer.getClientDetailsUrl());

    // Set the "JSESSIONID" cookie
    httpPost.setHeader(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);

    // Set the request body
    ObjectMapper objectMapper = new ObjectMapper();
    String requestBodyJson = null;
    try {
        requestBodyJson = objectMapper.writeValueAsString(requestBody);
    } catch (JsonProcessingException e) {
        return new ResponseEntity<>("Error converting request body to JSON: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    httpPost.setEntity(new StringEntity(requestBodyJson, ContentType.APPLICATION_JSON));

    // Send the request and get the response
    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    } catch (IOException e) {
        return new ResponseEntity<>("Error sending HTTP request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

 */
	
	
	
}