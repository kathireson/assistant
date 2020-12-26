package com.assistant.tasks.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorityUpdate {
	String taskId; // Task whose priority is to be updated
	String destinationDate;
	int desiredIndex;
}
