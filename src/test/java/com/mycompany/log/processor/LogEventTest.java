package com.mycompany.log.processor;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

public class LogEventTest {

	LogEventConfig logEventConfig;

	LogEventRepository logEventRepository;

	List<LogEvent> events;

	public LogEventTest() {
		Path logFilePath = Paths.get(System.getProperty("user.dir"),"/target/log-file.txt");
		logEventConfig = new LogEventConfig();
		logEventConfig.setFilePath(logFilePath);
		logEventConfig.setFilePath(logFilePath);
		logEventConfig.setNumOfRecords(1000);
	}

	@Before
	public void init() {
		LogEventRecordGenerator generator = new LogEventRecordGenerator(logEventConfig);
		events = generator.generateRecords();
		logEventRepository = Mockito.mock(LogEventRepository.class);
		LogEventEntity entity = new LogEventEntity();
		Mockito.when(logEventRepository.save(any(LogEventEntity.class))).thenReturn(entity);
	}

	@Test
	public void performTest() {
		LogEventProcessor processor = new LogEventProcessor(logEventConfig, logEventRepository);
		// all event ids having diff in start and finished timestamp > 4 ms
		List<String> evenIds = processor.processRecord();
		//collect all started event records for all processed event Id.
		Map<String, Long> startedEvents = events.stream().filter(event -> evenIds.contains(event.getId()) && event.getState().equals("STARTED"))
						.collect(Collectors.toMap(LogEvent::getId, LogEvent::getTimestamp));
		//collect all finished event records for all processed event Id.
		Map<String, Long> finishedEvents = events.stream().filter(event -> evenIds.contains(event.getId()) && event.getState().equals("FINISHED"))
						.collect(Collectors.toMap(LogEvent::getId, LogEvent::getTimestamp));
		evenIds.forEach(id -> {
			Assert.assertTrue((finishedEvents.get(id) - startedEvents.get(id)) > 4 );
		});
	}
}
