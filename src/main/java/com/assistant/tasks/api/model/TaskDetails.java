package com.assistant.tasks.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.assistant.tasks.data.model.Task;
import com.assistant.tasks.data.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDetails {
	String id;
	String title;
	String description;

	@JsonFormat(timezone="PST")
	Date createdDate;

	long updatedTime;
	ArrayList<String> tags;
	Long priority;
	TaskStatus status;
	boolean current;
	
	public TaskDetails(){
		// to allow for simple construction
	}
	
	public TaskDetails(Task task){
		this.id = task.getId();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.createdDate = task.getCreatedDate();
		this.updatedTime = task.getUpdatedTime();
		this.status = task.getStatus();
		this.current = task.isCurrent();
		this.priority = task.getPriority();
		if(StringUtils.hasText(task.getTags())) {
			this.tags = new ArrayList<String>(Arrays.asList(task.getTags().split(",")));
		}
	}
	
	public Task toDBModel() {
		Task task = new Task();
		task.setId(this.id);
		task.setTitle(this.title);
		task.setDescription(this.description);
		task.setCurrent(this.current);
		task.setStatus(this.status);
		task.setCreatedDate(this.createdDate);
		task.setUpdatedTime(this.updatedTime);
		task.setPriority(this.priority);
		if(this.tags != null && !this.tags.isEmpty()) {
			task.setTags(this.tags.stream().collect(Collectors.joining(",")));
		}
		return task;
	}
}
