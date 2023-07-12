package com.restapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.restapi.entity.RestEntity;
import com.restapi.repository.RestRepo;

@Service

public class EmployeeService {

	@Autowired
	private RestRepo restRepo;
	
	public List<RestEntity> getAll() {
		return restRepo.findAll();
	}
	
	public void newEmp(RestEntity restEntity) {
		this.restRepo.save(restEntity);
	}
	
	public RestEntity getById(long id) {
		Optional<RestEntity> optional=restRepo.findById(id);
	    RestEntity restEntity=null;
	    if(optional.isPresent()) {
	    	restEntity=optional.get();;
	    			
	    }
	    else {
	    	throw new RuntimeException("Employee not found for id.."+id);
	    }
	    return restEntity;
		
	}
	
	public void delEmp(long id) {
		this.restRepo.deleteById(id);
	}
}
