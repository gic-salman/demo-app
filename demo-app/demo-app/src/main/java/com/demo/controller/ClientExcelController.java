package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.ClientServiceImpl;

@RestController
public class ClientExcelController {
	
	@Autowired
	private ClientServiceImpl clientListServiceImpl;
	
	@GetMapping("/client-excel")
	public String generateClientExcel() {
		
		return clientListServiceImpl.readFromExcelFile();
	}
	
	@GetMapping("/questiona-builder-response")
	public String generateQuestionBuilderRes() {
		
		return clientListServiceImpl.generateQuestionaireBuilder();
	}

}
