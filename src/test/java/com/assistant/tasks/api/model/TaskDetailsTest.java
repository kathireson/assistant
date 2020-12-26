package com.assistant.tasks.api.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.assistant.tasks.data.model.Task;
import com.assistant.tasks.data.model.TaskStatus;

public class TaskDetailsTest {

	@Test
	public void testCreationFromDBModel() {
		// create a DB Task object
		Task task = createDBTask();
		
		// no tags should be fine
		TaskDetails details = new TaskDetails(task);	
		Assert.assertEquals(details.getId(), task.getId());
		Assert.assertEquals(details.getTitle(), task.getTitle());
		Assert.assertEquals(details.getCreatedDate(), task.getCreatedDate());
		Assert.assertEquals(details.getUpdatedTime(), task.getUpdatedTime());
		Assert.assertEquals(details.getPriority(), task.getPriority());
		Assert.assertEquals(details.isCurrent(), task.isCurrent());
		Assert.assertEquals(details.getStatus(), task.getStatus());
		Assert.assertEquals(details.getDescription(), task.getDescription());
		Assert.assertNull(details.getTags());
		// tags should be split properly
		task.setTags("tag1,tag2,tag3");
		details = new TaskDetails(task);
		Assert.assertNotNull(details.getTags());
		Assert.assertEquals(details.getTags().get(0), "tag1");
		Assert.assertEquals(details.getTags().get(1), "tag2");
		Assert.assertEquals(details.getTags().get(2), "tag3");
		
		// only one tag
		task.setTags("tag1");
		details = new TaskDetails(task);
		Assert.assertEquals(details.getTags().size(), 1);
		Assert.assertEquals(details.getTags().get(0), "tag1");
		
		// empty string
		task.setTags("");
		details = new TaskDetails(task);
		Assert.assertNull(details.getTags());
		// only spaces
		task.setTags("   ");
		details = new TaskDetails(task);
		Assert.assertNull(details.getTags());
	}
	
	@Test
	public void testDBModelCreation() {
		// create a API model
		TaskDetails details = new TaskDetails(createDBTask());
		
		// check without tags
		Task task = details.toDBModel();
		Assert.assertEquals(details.getId(), task.getId());
		Assert.assertEquals(details.getTitle(), task.getTitle());
		Assert.assertEquals(details.getCreatedDate(), task.getCreatedDate());
		Assert.assertEquals(details.getUpdatedTime(), task.getUpdatedTime());
		Assert.assertEquals(details.getPriority(), task.getPriority());
		Assert.assertEquals(details.isCurrent(), task.isCurrent());
		Assert.assertEquals(details.getStatus(), task.getStatus());
		Assert.assertEquals(details.getDescription(), task.getDescription());
		Assert.assertNull(task.getTags());
		
		// set Tags
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("tag1");
		tags.add("tag2");
		tags.add("tag3");
		
		details.setTags(tags);
		task = details.toDBModel();
		Assert.assertEquals(task.getTags(), "tag1,tag2,tag3");
		
		// empty list
		tags = new ArrayList<String>();
		details.setTags(tags);
		task = details.toDBModel();
		Assert.assertNull(task.getTags());
	}

	private Task createDBTask() {
		Task task = new Task();
		task.setId("123456");
		task.setTitle("Title");
		task.setCreatedDate(Date.from(Instant.now()));
		task.setUpdatedTime(Instant.now().toEpochMilli());
		task.setPriority(10000L);
		task.setCurrent(true);
		task.setStatus(TaskStatus.IN_PROGRESS);
		task.setDescription("Description");
		return task;
	}
	
}
