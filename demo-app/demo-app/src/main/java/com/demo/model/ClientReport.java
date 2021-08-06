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
@Table(name="client_report")
public class ClientReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3932024200291410904L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private Long clientId;
	
	@Column(name = "client_code")
	private String clientCode;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "id")
	private String id;
	
	@Column(name = "customer_id")
	private String customerId;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "sbu")
	private String sbu;
	
	@Column(name = "sbu_name")
	private String sbuName;
	
	@Column(name = "package_name")
	private String packageName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "linked_package")
	private String linkedPackage;
	
	@Column(name = "is_addon_package")
	private Boolean isAddonPackage;
	
	@Column(name = "allow_data_fetch_cspi")
	private Boolean allowDataFetchCSPi;
	
}
