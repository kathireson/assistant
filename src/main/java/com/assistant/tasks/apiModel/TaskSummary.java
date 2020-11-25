package com.assistant.tasks.apiModel;

import java.util.ArrayList;
import com.assistant.tasks.model.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskSummary {
	String date;
	ArrayList<Task> tasks;
}
