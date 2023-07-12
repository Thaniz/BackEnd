package com.restapi.pojo;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ClientPojo {

	 private String branchcode;
	    private String mobileno;
	    private String emailid;
	    private String clientId;
}
