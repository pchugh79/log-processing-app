package com.mycompany.log.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class LogEventRecordGenerator {

	private LogEventConfig config;
	private final int randomNumber;
	private ObjectMapper objectMapper;

	private Function<Object, String > objectToJsonFunction = (o) -> {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("unable to convert object to json", e);
		}
	};

	private Consumer<LogEvent> inputRecordConsumer = (o) -> {
		try {
			Files.write(
							config.getFilePath(),
							String.format("%s%s", objectToJsonFunction.apply(o), "\n").getBytes(StandardCharsets.UTF_8),
							StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException("Error writing records to file", e);
		}
	};

	LogEventRecordGenerator(LogEventConfig config) {
		this.config = config;
		// depending upon generated Random number each 100 records may have 100/<random-number> record execution
		// time > 4 ms
		this.randomNumber = new Random().nextInt(100);
		this.objectMapper = new ObjectMapper();
		if (Files.exists(config.getFilePath())) {
			try {
				Files.delete(config.getFilePath());
			} catch (IOException e) {
				throw new RuntimeException("Error writing records to file", e);
			}
		}
	}

	int getRandomNumber() {
		return randomNumber;
	}

	List<LogEvent> generateRecords() {

		List<LogEvent> logEventList = new ArrayList<>();

		LongStream.range(0, config.getNumOfRecords()).forEach(c -> {
			LogEvent startedLogEvent = new LogEvent(String.valueOf(c), "STARTED", new Date().getTime(), "APPLICATION_LOG", "12345");
			LogEvent finishedLogEvent = new LogEvent(startedLogEvent.getId(), "FINISHED",
							Long.parseLong(startedLogEvent.getId()) % randomNumber == 0 ? new Date(startedLogEvent.getTimestamp()+ 5000L).getTime() : startedLogEvent.getTimestamp(), "APPLICATION_LOG", "12345");
			inputRecordConsumer.accept(startedLogEvent);
			inputRecordConsumer.accept(finishedLogEvent);
			logEventList.add(startedLogEvent);
			logEventList.add(finishedLogEvent);
		});
		// for testing
		return logEventList;
	}
}
