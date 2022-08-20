package com.mycompany.log.processor;

import java.nio.file.Path;

public class LogEventConfig {

	private Path filePath;
	private long numOfRecords;
	private String executionMode;

	public LogEventConfig() {
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public long getNumOfRecords() {
		return numOfRecords;
	}

	public void setNumOfRecords(long numOfRecords) {
		this.numOfRecords = numOfRecords;
	}

	public String getExecutionMode() {
		return executionMode;
	}

	public void setExecutionMode(String executionMode) {
		this.executionMode = executionMode;
	}

	@Override
	public String toString() {
		return "LogEventConfig{" +
						"filePath='" + filePath + '\'' +
						", numOfRecords=" + numOfRecords +
						", executionMode='" + executionMode + '\'' +
						'}';
	}
}
