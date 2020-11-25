package com.assistant.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeMap;

import com.assistant.tasks.apiModel.TaskSummary;
import com.assistant.tasks.model.Task;

public class Utils {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String formatDate(Date dateObj){
		return dateFormat.format(dateObj);
	}
	
	public static Date parseDate(String dateStr) throws ParseException{
		dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		return dateFormat.parse(dateStr);
	}
	
	public static void sortTasksOnPriority(ArrayList<Task> taskList){
		taskList.sort(new Comparator<Task>(){
			@Override
			public int compare(Task o1, Task o2) {
				return Long.compare(o1.getPriority(), o2.getPriority());
			}
		});
	}
	
	public static ArrayList<TaskSummary> convertMapToTaskList(TreeMap<String, ArrayList<Task>> map){
		ArrayList<TaskSummary> taskSummaryList= new ArrayList<>();
		map.keySet().stream()
		.forEach(key ->{
			taskSummaryList.add(new TaskSummary(key, map.get(key)));
		});
		return taskSummaryList;
	}
	
}
