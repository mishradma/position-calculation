package com.org.mycompany.positions.services;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.org.mycompany.positions.domain.Position;
import com.org.mycompany.positions.repository.PositionRepository;

@Component(PositionFileProcessorService.SERVICE_ACTIVATOR)
public class PositionFileProcessorService implements FileProcessor {
	public static final String SERVICE_ACTIVATOR = "PositionFileProcessorService";
	@Autowired
	private PositionRepository repository;

	@Override
	public void process(Message<String> msg) {
		List<String> contentList = extractLines(msg);
		final List<Position> positionValues = Lists.newArrayList();
		List<Position> existingRecord = Lists.newArrayList(repository.findAll());
		if (CollectionUtils.isNotEmpty(existingRecord)) {
			// Since we are doing processing for POC purpose so delete old record
			repository.deleteAll(existingRecord);
		}
		contentList.subList(1, contentList.size()).forEach(action -> {
			String[] contentArr = action.split(",");
			Position obj = new Position(contentArr[0], contentArr[1], contentArr[2], Long.parseLong(contentArr[3]));
			positionValues.add(obj);
		});
		repository.saveAll(positionValues);
		// positionValues.clear();
		// positionValues.addAll(Lists.newArrayList(repository.findAll()));
		// positionValues.forEach(action -> System.out.println(action));

	}

}
