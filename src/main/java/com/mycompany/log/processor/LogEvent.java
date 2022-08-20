package com.mycompany.log.processor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEvent {
	@JsonProperty("id")
	private String id;
	@JsonProperty("state")
	private String state;
	@JsonProperty("timestamp")
	private Long timestamp;
	@JsonProperty("type")
	private String type;
	@JsonProperty("host")
	private String host;


	public LogEvent() {
	}

	LogEvent(String id, String state, Long timestamp, String type, String host) {
		this.id = id;
		this.state = state;
		this.timestamp = timestamp;
		this.type = type;
		this.host = host;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "LogEvent{" +
						"id='" + id + '\'' +
						", state='" + state + '\'' +
						", timestamp=" + timestamp +
						", type='" + type + '\'' +
						", host='" + host + '\'' +
						'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LogEvent logEvent = (LogEvent) o;
		return Objects.equals(id, logEvent.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
