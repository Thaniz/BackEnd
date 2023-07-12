package com.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restapi.entity.RestEntity;

@Repository
public interface RestRepo extends JpaRepository<RestEntity, Long>{
	
	

}
