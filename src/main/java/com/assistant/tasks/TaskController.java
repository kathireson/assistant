package com.assistant.tasks;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assistant.tasks.api.model.PriorityUpdate;
import com.assistant.tasks.api.model.TaskResponse;
import com.assistant.tasks.api.model.TaskSummary;
import com.assistant.tasks.data.TaskDao;
import com.assistant.tasks.data.model.Task;
import com.assistant.tasks.data.model.TaskStatus;

@RestController
public class TaskController {
	
	private static final long PRIORITY_INTERVAL = 10000L;
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	TaskDao taskDao;
	
	@GetMapping("/task/{taskId}") // Not used as of now
	public Task getTaskDetails(@PathVariable("taskId") String taskId){
		return taskDao.getTask(taskId);
	}
	
	@GetMapping("/task/report")
	public List<Task> getTaskReport(@RequestParam("forDate") String forDateStr) throws ParseException{
		Date forDate = Utils.parseDate(forDateStr);
		return taskDao.getTasksFromDate(forDate, false);
	}
	
	@GetMapping("/tasks")
	public List<TaskSummary> getAllPendingTasks(){
		TreeMap<String, ArrayList<Task>> taskMap = new TreeMap<>();
		taskDao.getNotDoneTasks()
			.stream().forEach(t -> {
				String strDate = Utils.formatDate(t.getCreatedDate());
				if(taskMap.containsKey(strDate)){
					taskMap.get(strDate).add(t);
				}else{
					taskMap.put(strDate,new ArrayList<Task>(Arrays.<Task>asList(t)));
				}
			});
		// if current date is not in the map
		// add current date too with empty task list
		String currentDate = Utils.formatDate(new Date());
		if(!taskMap.containsKey(currentDate)){
			taskMap.put(currentDate, new ArrayList<Task>());
		}
		// sort tasks based on priority for each date
		taskMap.forEach((date, taskList) -> {
			Utils.sortTasksOnPriority(taskList);
		});
		return Utils.convertMapToTaskList(taskMap);
	}

	@GetMapping("/task") // not used as of now
	public List<Task> getTasksWithinTime(@RequestParam("fromDate") String fromDateStr, @RequestParam("toDate") String toDateStr) throws ParseException{
		Date fromDate = Utils.parseDate(fromDateStr);
		Date toDate = Utils.parseDate(toDateStr);
		
		System.out.println("fromDate " + fromDate + ",toDate : " + toDate);
		return taskDao.getTasks(fromDate, toDate);
	}
	
	@PostMapping("/task")
	public TaskResponse createTask(@RequestBody Task task){
		Date currentTime = new Date();
		// if created time is not there - add it
		if(task.getCreatedDate() == null){
			task.setCreatedDate(currentTime);
		}
		task.setUpdatedTime(currentTime.getTime());
		
		updatePriority(task);
		
		TaskResponse response = new TaskResponse(taskDao.createTask(task));
		
		// update if this is current job
		if(task.isCurrent() && !TaskStatus.DONE.equals(task.getStatus())){
			taskDao.markAsCurrent(task.getId());
		}

		return response;
	}
	
	@PutMapping("/task/{taskId}")
	public void updateTask(@PathVariable("taskId") String taskId, @RequestBody Task task){
		Date currentTime = new Date();
		task.setUpdatedTime(currentTime.getTime());
		if(task.getCreatedDate() == null){
			task.setCreatedDate(currentTime);
		}
		// if date changed during update 
		// or - set priority to minimum
		Task oldTask = taskDao.getTask(taskId);
		if(task.getPriority() == null || (oldTask != null && !oldTask.getCreatedDate().equals(task.getCreatedDate()))){
			updatePriority(task);
		}
		taskDao.updateTask(taskId, task);
		// update if this is current job
		if(task.isCurrent() && !TaskStatus.DONE.equals(task.getStatus())){
			taskDao.markAsCurrent(taskId);
		}
	}

	private void updatePriority(Task task) {
		Long minPriority = taskDao.getMinPriorityInDate(task.getCreatedDate());
		if(minPriority != null){
			task.setPriority(minPriority + PRIORITY_INTERVAL);
		} else {
			task.setPriority(0L);
		}
	}

	@DeleteMapping("/task/{taskId}")
	public void deleteTask(@PathVariable("taskId") String taskId){
		taskDao.deleteTask(taskId);
	}
	
	@PostMapping("task/priorityChange")
	public void updatePriority(@RequestBody PriorityUpdate priorityMap) throws ParseException{

		Task task = taskDao.getTask(priorityMap.getTaskId());
		if(task != null){

			Date destinationDate = Utils.parseDate(priorityMap.getDestinationDate());
			task.setCreatedDate(destinationDate);
			int desiredIndex = priorityMap.getDesiredIndex();
			// find the task at desired index and desired index -1
			List<Task> taskList = taskDao.getTasksFromDate(destinationDate, true);
			// no other tasks - or we want current task to be in top
			if(taskList == null || taskList.isEmpty()){

				task.setPriority(0L);
			} else {

				// set priority of current task to a value between the two queried earlier
				ArrayList<Task> dayTasks = new ArrayList<Task>(taskList);
				Utils.sortTasksOnPriority(dayTasks);
				if(desiredIndex >= (dayTasks.size() - 1)){

					task.setPriority(dayTasks.get(dayTasks.size()-1).getPriority() + PRIORITY_INTERVAL);
				} else {
					if(desiredIndex == 0){
						task.setPriority(dayTasks.get(0).getPriority() - PRIORITY_INTERVAL);
					} else {
						// check next task has to move into current task's index
						int currentIndex = getCurrentIndex(dayTasks, task);
						long prevPriority = 0L;
						long nextPriority = 0L;
						if(currentIndex != -1){

							if(currentIndex == desiredIndex){

								return;
							} else if(currentIndex < desiredIndex){

								// next task has to move up to current
								prevPriority = dayTasks.get(desiredIndex).getPriority();
								nextPriority = dayTasks.get(desiredIndex + 1).getPriority();
							} else {

								prevPriority = dayTasks.get(desiredIndex - 1).getPriority();
								nextPriority = dayTasks.get(desiredIndex).getPriority();
							}
						} else {
							// coming from another day - nothing needs to move up
							prevPriority = dayTasks.get(desiredIndex - 1).getPriority();
							nextPriority = dayTasks.get(desiredIndex).getPriority();
						}
						if((nextPriority-prevPriority) <=1 ){
							// some person has added more than 10000 tasks in a day
							// and re-prioritized tasks 10000 times !!!
							// screw that person!! - I'm not updating DB for that use case now

						} else {

							task.setPriority((prevPriority + ((nextPriority - prevPriority)/2 )));
						}
					}
				}			
			}
			// save current task
			taskDao.updateTask(task.getId(), task);
		}
	}
	
	private int getCurrentIndex(List<Task> taskList, Task current){
		for(int i = 0; i < taskList.size(); i++){
			if(taskList.get(i).getId().equals(current.getId())){
				return i;
			}
		}
		return -1;
	}
	
	@PostMapping("task/{taskId}/current")
	public void makeCurrent(@PathVariable("taskId") String taskId){

		// mark current taskId as current
		taskDao.markAsCurrent(taskId);
	}
	
	@PostMapping("task/{taskId}/done")
	public void markDone(@PathVariable("taskId") String taskId){

		// mark current taskId as current
		taskDao.markAsDone(taskId);
	}
	
	@PostMapping("task/maxPriority")
	public String getMaxPriority(@RequestBody Task task){
		return String.valueOf(taskDao.getMinPriorityInDate(task.getCreatedDate()));
	}
}
