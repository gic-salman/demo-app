package com.demo.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.model.ClientReport;
import com.demo.model.QbResponse;
import com.demo.pojo.Clients;
import com.demo.pojo.QuestionBuilderResponse;
import com.demo.pojo.QuestionBuilser;
import com.demo.repository.ClientReportRepository;
import com.demo.repository.QbResponseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ClientServiceImpl implements ClientService {

	@Value("${file.dir}")
	private String excelFilePath;

	@Value("${amsc.cust.url}")
	private String custUrl;
	
	@Value("${amsc.qb.url}")
	private String questionBuilder;

	@Autowired
	private ApiService apiService;

	@Autowired
	private ClientReportRepository clientReportRepository;
	
	@Autowired
	private QbResponseRepository qbResponseRepository;

	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	@Override
	public String readFromExcelFile() {

		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			List<Clients> clientList = new ArrayList<>();
			Iterator<Row> iterator = sheet.iterator();
			boolean isHeader = true;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (isHeader) {
					isHeader = false;
					continue;
				}
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				Clients client = new Clients();
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					if (columnIndex == 0) {
						DataFormatter formatter = new DataFormatter();
						String cellValue = formatter.formatCellValue(nextCell);
						if(StringUtils.isNotEmpty(cellValue))
						  client.setClientCode(cellValue);
						
					} else {
						DataFormatter formatter = new DataFormatter();
						String cellValue = formatter.formatCellValue(nextCell);
						if(StringUtils.isNotEmpty(cellValue))
						client.setClientName(cellValue);
					}
				}
				clientList.add(client);

			}
			workbook.close();
			inputStream.close();
			logger.info("ClientName list with clientCode : {}", clientList);

			for (Clients client : clientList) {
				
				ObjectMapper mapper = new ObjectMapper();
				StringBuilder sb = new StringBuilder();
				 JsonNode node=null;
				 String finalUrl=null;
				 try {
					
					  finalUrl = sb.append(custUrl).append("/customersbu").append("/").append(client.getClientName())
							 .append("/customerName").toString();
					 String response = apiService.sendDataToL3Get(finalUrl);
					 logger.info("api response : {}", response);
					 node = mapper.readValue(response, JsonNode.class);
					 
				} catch (Exception e) {
					logger.info("Exception Occured while calling url : {}",finalUrl);
					e.printStackTrace();
				}
				

				Boolean isSuccess =node.get("success")!=null? node.get("success").asBoolean():false;

				if (isSuccess) {
					
					JsonNode jsonNode = node.get("response")!=null? node.get("response"):null;
					
					if(jsonNode!=null) {
						for (JsonNode nodes1 : jsonNode) {							
							JsonNode node2=null;
							String finalUrl2=null;
							try {
								
								String sbu1 = nodes1.get("sbu") != null ? nodes1.get("sbu").asText() : "";
								StringBuilder sb2 = new StringBuilder();
								finalUrl2 = sb2.append(custUrl).append("/customersbupackage").append("/")
										.append(client.getClientName()).append("/").append(sbu1).append("/findpackages/")
										.toString();
								
								String response2 = apiService.sendDataToL3Get(finalUrl2);
								logger.info("api response : {}", response2);
								 node2 = mapper.readValue(response2, JsonNode.class);
								
							}catch(Exception e) {
								
								logger.info("Exception Occured while calling url : {}",finalUrl2);
								e.printStackTrace();
							}
							Boolean isSuccess2 = node2.get("success") != null ? node2.get("success").asBoolean() : false;

							if (isSuccess2) {
								
								try {
									
									JsonNode jsonNode2 =node2.get("response")!=null?node2.get("response"):null;
									
									if(jsonNode2!=null) {
										for (JsonNode nodes2 : jsonNode2) {
											String id = nodes2.get("id") != null ? nodes2.get("id").asText() : null;
											String customerId = nodes2.get("customerId") != null ? nodes2.get("customerId").asText()
													: null;
											String customerName = nodes2.get("customerName") != null
													? nodes2.get("customerName").asText()
															: null;
											String sbu2 = nodes2.get("sbu") != null ? nodes2.get("sbu").asText() : null;
											String sbuName = nodes2.get("sbuName") != null ? nodes2.get("sbuName").asText() : null;
											String packageName = nodes2.get("packageName") != null
													? nodes2.get("packageName").asText()
															: null;
											String description = nodes2.get("description") != null
													? nodes2.get("description").asText()
															: null;
											String linkedPackage = nodes2.get("linkedPackage") != null
													? nodes2.get("linkedPackage").asText()
															: null;
											Boolean isAddonPackage = nodes2.get("isAddonPackage") != null
													? nodes2.get("isAddonPackage").asBoolean()
															: false;
											Boolean allowDataFetchCSPi = nodes2.get("allowDataFetchCSPi") != null
													? nodes2.get("allowDataFetchCSPi").asBoolean()
															: false;

											ClientReport clientReport = new ClientReport();
											clientReport.setClientCode(client.getClientCode());
											clientReport.setClientName(client.getClientName());
											clientReport.setId(id);
											clientReport.setCustomerId(customerId);
											clientReport.setCustomerName(customerName);
											clientReport.setSbu(sbu2);
											clientReport.setSbuName(sbuName);
											clientReport.setPackageName(packageName);
											clientReport.setDescription(description);
											clientReport.setLinkedPackage(linkedPackage);
											clientReport.setIsAddonPackage(isAddonPackage);
											clientReport.setAllowDataFetchCSPi(allowDataFetchCSPi);
											clientReportRepository.save(clientReport);
//											clientReportList.add(clientReport);
										}
										
									}else {	
										logger.info("Response not found : {}");
									}	
								}catch (Exception e) {
									logger.info("Exception Occured while saving new record : {}");
									e.printStackTrace();
								}
							}
						} // end of loop
						
					}else {
						logger.info("Response not found : {}");
					}
					
				} // end of if
			} // end of outer loop

			logger.info("Client report saved successfully  : {}");
			return "Client report saved successfully !!";
			
		} catch (Exception e) {

			logger.error("Exception Occured while saving client report : {}", e);
			e.printStackTrace();
		}
		return "Something went wrong !!";
	}
	
	@Override
	public String generateQuestionaireBuilder() {
		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			List<Clients> clientList = new ArrayList<>();
			Iterator<Row> iterator = sheet.iterator();
			boolean isHeader = true;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (isHeader) {
					isHeader = false;
					continue;
				}
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				Clients client = new Clients();
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();
					if (columnIndex == 0) {
						DataFormatter formatter = new DataFormatter();
						String cellValue = formatter.formatCellValue(nextCell);
						if(StringUtils.isNotEmpty(cellValue))
						  client.setClientCode(cellValue);
						
					} else {
						DataFormatter formatter = new DataFormatter();
						String cellValue = formatter.formatCellValue(nextCell);
						if(StringUtils.isNotEmpty(cellValue))
						client.setClientName(cellValue);
					}
				}
				clientList.add(client);// get list of ClientId and clientCode

			}
			workbook.close();
			inputStream.close();

			logger.info("ClientName list with clientCode : {}", clientList);
			for (Clients client : clientList) {
				
				 ObjectMapper mapper = new ObjectMapper();
				 StringBuilder sb = new StringBuilder();
				 JsonNode node1=null;
				 String finalUrl=null;
				 Boolean isSuccess=false;
				 try {
					
					  finalUrl = sb.append(custUrl).append("/customersbu").append("/").append(client.getClientName())
							 .append("/customerName").toString();
					 String response = apiService.sendDataToL3Get(finalUrl);
					 logger.info("api response 1 : {}", response);
					 node1 = mapper.readValue(response, JsonNode.class);
					 isSuccess =node1.get("success")!=null? node1.get("success").asBoolean():false;
					 
				} catch (Exception e) {
					logger.info("Exception Occured while calling url : {}",finalUrl);
					e.printStackTrace();
				}
				if (isSuccess) {
					JsonNode jsonNode = node1.get("response")!=null? node1.get("response"):null;
					if(jsonNode!=null) {	
						for (JsonNode jsonNode1 : jsonNode) {
							JsonNode jsonNode2=null;
							String finalUrl2="";
							JsonNode jsonNode2Res=null;
							Boolean isSuccess2=false;
							
							try {
								String sbu1 = jsonNode1.get("sbu") != null ? jsonNode1.get("sbu").asText() : "";
								StringBuilder sb2 = new StringBuilder();
								finalUrl2 = sb2.append(custUrl).append("/customersbupackage").append("/")
										.append(client.getClientName()).append("/").append(sbu1).append("/findpackages/")
										.toString();
								
								 String response2 = apiService.sendDataToL3Get(finalUrl2);
								 logger.info("api response 2: {}", response2);
								 jsonNode2 = mapper.readValue(response2, JsonNode.class);	
								 isSuccess2 = jsonNode2.get("success") != null ? jsonNode2.get("success").asBoolean() : false;
								 jsonNode2Res =jsonNode2.get("response")!=null?jsonNode2.get("response"):null;
								 
							}catch(Exception e) {
								logger.info("Exception Occured while calling url : {}",finalUrl2);
								e.printStackTrace();
							}
							if (isSuccess2) {
								try {
									if(jsonNode2Res!=null) {
										for (JsonNode nodes2 : jsonNode2Res) {
											//JsonNode node3=null;
											String finalUr3=null;	
											try {
												String customer =  nodes2.get("customerName").asText();
												String sbuName =  nodes2.get("sbuName").asText();
												String packageName = nodes2.get("packageName").asText();
												
												StringBuilder sb3 = new StringBuilder();
												finalUr3 = sb3.append(questionBuilder)
														.append(customer).append("/").append(sbuName).append("/").append(packageName)
														.toString();
												
												//finalUr3="https://amcspingap01lvp.emea.fadv.net/Cspi/api/qb/RPA Testing/RPA Testing/Package 1 8-Jan-15 SRTA616RPA";
												String response3 = apiService.sendDataToL3Get(finalUr3);
												logger.info("api response3 : {}", response3);
//												JsonNode jsonNode2 =node2.get("response")!=null?node2.get("response"):null;
												QuestionBuilderResponse	questionBuilderResponse = mapper.readValue(response3, QuestionBuilderResponse.class);
												//QuestionBuilderResponse	node3 = mapper.converValue(response3, new TypeReference<QuestionBuilderResponse>);
												List<QuestionBuilser> qbbuilderList=questionBuilderResponse.getResponse();
												List<QbResponse> qbResponseList=new ArrayList<>();
												
												 if(CollectionUtils.isNotEmpty(qbbuilderList)) {
													 
													 for(QuestionBuilser questionBuilser:qbbuilderList) {
														 QbResponse qbResponse= new QbResponse();
														 BeanUtils.copyProperties(questionBuilser, qbResponse);
														 qbResponseList.add(qbResponse);
													 }
													
													qbResponseRepository.saveAll(qbResponseList);
												}
											}catch(Exception ex) {
												
												logger.info("Exception Occured while saving new record : {}");
												ex.printStackTrace();
											}
										}
									}else {	
										logger.info("Response not found : {}");
									}	
								}catch (Exception e) {
									logger.info("Exception Occured while getting record from  : {}",finalUrl2);
									e.printStackTrace();
								}
							}
						} // end of loop
						
					}else {
						logger.info("Response not found : {}");
					}
					
				} // end of if
			} // end of outer loop
			logger.info("Client report saved successfully  : {}");
			return "Client report saved successfully !!";
			
		} catch (Exception e) {

			logger.error("Exception occured : {}", e);
			e.printStackTrace();
		}
		return "Something went wrong !!";
	}
}
