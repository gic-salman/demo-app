package com.demo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "qb_response")
public class QbResponse  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5167408762226092882L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qb_id")
	private Long qbId;

	private String id;
	private String questionName;
	private String questionScope;
	private String categoryName;
	private String categoryId;
	private String componentId;
	private String componentName;
	private String productId;
	private String productName;
	private String clientId;
	private String clientCode;
	private String clientName;
	private String sbuId;
	private String sbuName;
	private String packageId;
	private String packageName;
	private String formLabel;
	private String reportLabel;
	private String packageQuestion;
	private String ngDocumentId;
	private String ngDocumentName;
	private String ngDocumentFieldId;
	private String ngDocumentField;
	private String deType;
	private String questionType;
	private Integer sequenceId;
	private Boolean isMandatory;
	private Integer globalQuestionId;
	private String priorityId;
	private Boolean docPresent;
	private Boolean reportRequired;

}
