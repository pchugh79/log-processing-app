package com.mycompany.log.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import java.nio.file.Files;


@SpringBootApplication
public class LogEventProcessingApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogEventProcessingApplication.class);

	@Bean
	@ConfigurationProperties(prefix = "input")
	LogEventConfig logEventConfig() {
		return new LogEventConfig();
	}

	@Bean
	@ConditionalOnProperty(name = "input.execution-mode", havingValue = "generate-records")
	LogEventRecordGenerator inputRecordGenerator(LogEventConfig logEventConfig) {
		return new LogEventRecordGenerator(logEventConfig);
	}

	@Bean
	LogEventProcessor logEventProcessor(LogEventConfig logEventConfig, LogEventRepository repository) {
		return new LogEventProcessor(logEventConfig, repository);
	}

	public static void main(String[] args) {
		SpringApplication.run(LogEventProcessingApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(LogEventConfig config,
	 	@Nullable LogEventRecordGenerator logEventRecordGenerator, LogEventProcessor logEventProcessor,
		 LogEventRepository eventRepository) {
		LOGGER.debug("configuration - {}", config);
		return args -> {
			if (ExecutionMode.GENERATE_RECORDS.getMode().equals(config.getExecutionMode())) {
				assert logEventRecordGenerator != null;
				logEventRecordGenerator.generateRecords();
			}
			else {
				if (!Files.exists(config.getFilePath())) {
					throw new RuntimeException("Input file not found ");
				}
				logEventProcessor.processRecord();
				LOGGER.info("{} Number of records with Finished processing timestamp greater than 4 ms.", eventRepository.count());
				if (LOGGER.isDebugEnabled()) {
					eventRepository.findAll().forEach( record -> LOGGER.debug("Record : {}", record));
				}
			}
		};
	}
}
