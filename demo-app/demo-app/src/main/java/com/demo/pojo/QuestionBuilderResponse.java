package com.demo.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionBuilderResponse {

	private Boolean success;
	private String successMsg;
	private String successCode;
	private List<QuestionBuilser> response;
	
}
