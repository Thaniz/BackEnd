package com.restapi.pojo;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class EmployeePojo {
	
	private String name;
	private Long age;
	private Long salary;
}
