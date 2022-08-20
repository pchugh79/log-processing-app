package com.mycompany.log.processor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "event")
public class LogEventEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String eventId;
	private String type;
	private String host;
	private long eventDuration;

	LogEventEntity() {
	}

	LogEventEntity(String eventId, String type, String host, long eventDuration) {
		this.eventId = eventId;
		this.type = type;
		this.host = host;
		this.eventDuration = eventDuration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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

	public long getEventDuration() {
		return eventDuration;
	}

	public void setEventDuration(long eventDuration) {
		this.eventDuration = eventDuration;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LogEventEntity that = (LogEventEntity) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return
						"id=" + id +
						", eventId='" + eventId + '\'' +
						", type='" + type + '\'' +
						", host='" + host + '\'' +
						", eventDuration=" + eventDuration;
	}
}
