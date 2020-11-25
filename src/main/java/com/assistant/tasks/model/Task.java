package com.assistant.tasks.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String title;
	String description;

	@JsonFormat(timezone="PST")
	@Temporal(TemporalType.DATE)
	Date createdDate;

	long updatedTime;
	String tags;
	Long priority;
	TaskStatus status;
	boolean current;
}
