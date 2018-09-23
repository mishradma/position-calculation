/**
 * 
 */
package com.org.mycompany.positions.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.org.mycompany.positions.domain.Transaction;
import com.org.mycompany.positions.repository.TransactionRepository;

/**
 * @author Dayanand Mishra
 *
 */
@Component(TransactionFileProcessorService.SERVICE_ACTIVATOR)
public class TransactionFileProcessorService implements FileProcessor {
	public static final String SERVICE_ACTIVATOR = "TransactionFileProcessorService";
	private static final Logger LOG = LoggerFactory.getLogger(TransactionFileProcessorService.class);
	@Autowired
	private TransactionRepository repository;

	/**
	 * Below service should be cron job driven. But in interest of time and POC
	 * purpose adjusting calculation immediately after transaction reported.
	 */
	@Autowired
	private PositionCalculationService calculationService;

	@Override
	public void process(Message<String> msg) {
		// for POC purpose clearing transaction table
		repository.deleteAll();
		List<Transaction> contentList = parseMessage(msg);
		repository.saveAll(contentList);
		// Adjust position
		contentList.forEach(action -> System.out.println(action));
		calculationService.adjustEndOfTheDayaPosition();

	}

	private List<Transaction> parseMessage(Message<String> msg) {
		String content = msg.getPayload();
		List<Transaction> contentList = Collections.emptyList();
		ObjectMapper mapper = new ObjectMapper();
		try {
			CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class);
			contentList = mapper.readValue(content, javaType);
		} catch (IOException e) {
			LOG.error("Invalid iput file ", e);
		}
		return contentList;
	}

}
