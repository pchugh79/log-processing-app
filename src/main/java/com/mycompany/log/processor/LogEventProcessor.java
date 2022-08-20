package com.mycompany.log.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class LogEventProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogEventProcessor.class);

	private LogEventConfig config;
	private LogEventRepository eventRepository;

	LogEventProcessor(LogEventConfig config, LogEventRepository eventRepository) {
		this.config = config;
		this.eventRepository = eventRepository;
	}

	/**
	 * @return all event ids having FINISHING event logged after 4 ms.
	 */
	List<String> processRecord() {
		long startTime = System.nanoTime();
		ConcurrentHashMap<String, LogEvent> concurrentHashMap = new ConcurrentHashMap<>();
		// ensure entire file is not read in memory and load records on demand.
		List<String> eventIds = new ArrayList<>();
		try (Stream<String> lines = Files.lines(config.getFilePath(), StandardCharsets.UTF_8)) {
			lines.parallel().forEach(line -> {
				LogEvent logEvent = read(line);
				if (logEvent == null) {
					return;
				}
				if (concurrentHashMap.containsKey(logEvent.getId())) {
					long duration = compareLogEvent(concurrentHashMap.get(logEvent.getId()), logEvent);
					if (duration > 4) {
						eventRepository.save(new LogEventEntity(logEvent.getId(), logEvent.getType(), logEvent.getHost(), duration));
						concurrentHashMap.remove(logEvent.getId());
						eventIds.add(logEvent.getId());
					}
				}
				else {
					concurrentHashMap.put(logEvent.getId(), logEvent);
				}
			});
		} catch (IOException e) {
			throw new RuntimeException("Error while processing records", e);
		}
		LOGGER.debug("Records processing time {} nano-secs", (System.nanoTime() - startTime));
		return eventIds;
	}

	private LogEvent read(String s) {
		try {
			return new ObjectMapper().readValue(s, LogEvent.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private long compareLogEvent(LogEvent logEvent, LogEvent logEvent1) {
		// both should be same event
		if (logEvent.equals(logEvent1)) {
			// In started state
			if (logEvent.getState().equals("STARTED")) {
				return logEvent1.getTimestamp() - logEvent.getTimestamp();
			}
			return logEvent.getTimestamp() - logEvent1.getTimestamp();
		}
		// it should not reach hear
		return Long.MAX_VALUE;
	}
}
