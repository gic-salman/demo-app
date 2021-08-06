package com.demo.runner;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

	
	
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		MyRunner.generateUUID();
	}

	private static String generateUUID() {
		
		System.out.println( UUID.randomUUID().toString());
		return UUID.randomUUID().toString();
	}
}
