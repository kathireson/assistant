package com.assistant.tasks.api.model;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskSummary {
	String date;
	ArrayList<TaskDetails> tasks;
}
