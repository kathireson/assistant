package com.assistant.tasks.model;

import lombok.Getter;

@Getter
public enum TaskStatus {
	BACKLOG (0), 
	IN_PROGRESS (1),
	DONE(2);
	
	private int value;

	TaskStatus(int value){
		this.value = value;
	}
}
