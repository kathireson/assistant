package com.assistant.tasks.data;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.assistant.tasks.data.model.Task;
import com.assistant.tasks.data.model.TaskStatus;

@Component
public class TaskDao {

	@PersistenceContext
	private EntityManager eManager;

	@Transactional
	public String createTask(Task task){
		eManager.persist(task);
		eManager.flush();
		return task.getId();
	}
	
	public Task getTask(String taskId){
		return eManager.find(Task.class, taskId);
	}
	
	@Transactional
	public void updateTask(String taskId, Task task){
		Query q = eManager.createQuery("update Task set title= :title, description = :desc, createdDate = :ctime"
				+ ", updatedTime = :utime, priority = :priority, status = :status, tags = :tags where id = :id");
		q.setParameter("title", task.getTitle());
		q.setParameter("desc", task.getDescription());
		q.setParameter("ctime", task.getCreatedDate());
		q.setParameter("utime", task.getUpdatedTime());
		q.setParameter("status", task.getStatus());
		q.setParameter("priority", task.getPriority());
		q.setParameter("tags", task.getTags());
		q.setParameter("id", taskId);	
		q.executeUpdate();
	}
	
	@Transactional
	public void deleteTask(String taskId){
		Task tobeRemoved = eManager.find(Task.class, taskId);
		eManager.remove(tobeRemoved);
	}

	@SuppressWarnings("unchecked")
	public List<Task> getTasks(Date fromDate, Date toDate) {
		Query q = eManager.createQuery("from Task where createdDate > :fromDate "
				+ "and createdDate < :toDate "
				+ "and status != :status", Task.class);
		q.setParameter("fromDate", fromDate);
		q.setParameter("toDate", toDate);
		q.setParameter("status", TaskStatus.DONE);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Task> getTasksFromDate(Date fromDate, boolean filterDone) {
		String queryString = "from Task where createdDate = :fromDate" + (filterDone? " and status != :status" : "");
		Query q = eManager.createQuery(queryString, Task.class);
		q.setParameter("fromDate", fromDate);
		if(filterDone){
			q.setParameter("status", TaskStatus.DONE);
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Task> getNotDoneTasks() {
		Query q = eManager.createQuery("from Task where status != :status "
				+ "order by priority", Task.class);
		q.setParameter("status", TaskStatus.DONE);
		return q.getResultList();
	}

	@Transactional
	public void markAsCurrent(String taskId) {
		// clear current from all other tasks
		// I know this is a ugly hack - will rethink later when all else is working
		Query clearQuery = eManager.createQuery("update Task set current = false where current=true");
		clearQuery.executeUpdate();
		// mark only the current one as current
		Query q = eManager.createQuery("update Task set current = true, status = :status where id = :id");
		q.setParameter("id", taskId);
		q.setParameter("status", TaskStatus.IN_PROGRESS);
		q.executeUpdate();
	}
	
	@Transactional
	public void markAsDone(String taskId) {
		// mark only the current one as done
		Query q = eManager.createQuery("update Task set status = :status where id = :id");
		q.setParameter("id", taskId);
		q.setParameter("status", TaskStatus.DONE);
		q.executeUpdate();
	}
	
	public Long getMinPriorityInDate(Date date){
		return (Long) eManager.createQuery("select max(t.priority) from Task t where t.createdDate = :date"
				+ " and status != :status")
				.setParameter("date", date)
				.setParameter("status", TaskStatus.DONE)
				.getSingleResult();
	}
}
