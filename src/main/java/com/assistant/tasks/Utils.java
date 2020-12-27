package com.assistant.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assistant.tasks.api.model.TaskDetails;
import com.assistant.tasks.api.model.TaskSummary;
import com.assistant.tasks.data.model.Task;

public class Utils {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
	}

	public static String formatDate(Date dateObj){
		return dateFormat.format(dateObj);
	}
	
	public static Date parseDate(String dateStr){
		try {
			return dateFormat.parse(dateStr);
		} catch ( Exception e) {
			logger.error("Exception while parsing date : {}", e);
			return new Date();
		}
		
	}
	
	public static void sortTasksOnPriority(ArrayList<TaskDetails> taskList){
		taskList.sort(new Comparator<TaskDetails>(){
			@Override
			public int compare(TaskDetails o1, TaskDetails o2) {
				return Long.compare(o1.getPriority(), o2.getPriority());
			}
		});
	}
	
	public static void sortDBTasksOnPriority(ArrayList<Task> taskList){
		taskList.sort(new Comparator<Task>(){
			@Override
			public int compare(Task o1, Task o2) {
				return Long.compare(o1.getPriority(), o2.getPriority());
			}
		});
	}
	
	public static ArrayList<TaskSummary> convertMapToTaskList(TreeMap<String, ArrayList<TaskDetails>> map){
		// seriously - this need to be cleaned up later
		ArrayList<TaskSummary> taskSummaryList= new ArrayList<>();
		map.keySet().stream()
		.forEach(key ->{
			taskSummaryList.add(new TaskSummary(key, map.get(key)));
		});
		return taskSummaryList;
	}
	
}
