package com.restapi.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class UrlContainer {

	@Value("${ClientListDetails.url}")
	String clientDetailsUrl;
}
