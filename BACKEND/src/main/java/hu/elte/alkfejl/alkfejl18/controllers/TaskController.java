package hu.elte.alkfejl.alkfejl18.controllers;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hu.elte.alkfejl.alkfejl18.entities.*;
import hu.elte.alkfejl.alkfejl18.repositories.*;

@CrossOrigin
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private UserRepository userRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private SkillRepository skillRepository;
	
	@GetMapping("")
	public ResponseEntity<Iterable<Task>> getAll(){
		return ResponseEntity.ok(taskRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTask(@PathVariable Integer id){
		Optional<Task> oTask = taskRepository.findById(id);
		if(!oTask.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oTask.get());
	}
	
	@PostMapping("/new")
	public ResponseEntity createTask(@RequestBody MessageWrapper task,Authentication auth){
		String userName = auth.getName();
		if(!task.isTask()) {
			return ResponseEntity.notFound().build();
		}
		Optional<Project> oProject = projectRepository.findById(task.getProjectId());
		if(!oProject.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		if(userName.equals("admin") || userName.equals(oProject.get().getLeader().getUsername())) {
			ArrayList<Task> prerequisites = new ArrayList<Task>();
			ArrayList<Skill> requiredSkills = new ArrayList<Skill>();
			if(task.getRequiredSkills() != null) {
				for(Integer id : task.getRequiredSkills()) {
					Optional<Skill> oSkill = skillRepository.findById(id);
					if(oSkill.isPresent()) {
						requiredSkills.add(oSkill.get());
					}
				}
			}
			if(task.getPrerequisites() != null) {
				for(Integer id : task.getPrerequisites()) {
					Optional<Task> oTask = taskRepository.findById(id);
					if(oTask.isPresent()) {
						prerequisites.add(oTask.get());
					}
				}
			}
			Boolean isOpen = task.getIsOpen() == null ? false : task.getIsOpen();
			Task newTask = new Task(null,task.getName(),requiredSkills,new ArrayList<User>(),
					prerequisites,new ArrayList<Task>(),false,null,null,null,isOpen,oProject.get());
			oProject.get().getTasks().add(newTask);
			projectRepository.save(oProject.get());
			ResponseEntity result =  ResponseEntity.ok(taskRepository.save(newTask));
			for(Task t : prerequisites) {
				t.getRequiredBy().add(newTask);
				taskRepository.save(t);
			}
			for(Skill s : requiredSkills) {
				s.getRequiredBy().add(newTask);
				skillRepository.save(s);
			}
			return result;
		}else {
			return ResponseEntity.status(401).body("Only the project leader can add tasks to the project");
		}
	}
	
	@DeleteMapping("/{id}/delete")
	public ResponseEntity deleteTask(@PathVariable Integer id,Authentication auth){
		String userName = auth.getName();
		Optional<Task> oTask = taskRepository.findById(id);
		if(!oTask.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		if(userName.equals("admin") || userName.equals(oTask.get().getProject().getLeader().getUsername())) {
			Task task = oTask.get();
			if(task.getRequiredBy().size()>0) {
				return ResponseEntity.badRequest().body("The task can't be deleted since it is the prerequisite of other tasks");
			}
			for(User u : task.getAssignees()) {
				u.getAssignedTasks().remove(task);
				userRepository.save(u);
			}
			task.getProject().getTasks().remove(task);
			projectRepository.save(task.getProject());
			taskRepository.delete(task);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(401).body("Only the project leader can delete tasks");
		}
	}
	
	@PutMapping("/{id}/assign")
	public ResponseEntity assignTask(@PathVariable Integer id,Authentication auth,MessageWrapper muser){
		String userName = auth.getName();
		Optional<Task> oTask = taskRepository.findById(id);
		Optional<User> oUser = userRepository.findByUsername(muser.getUsername());
		if (!oUser.isPresent() || !oTask.isPresent()) {
            return ResponseEntity.notFound().build();
        }
		Task task = oTask.get();
		User user = oUser.get();
		Boolean userInProject = task.getProject().getMembers().contains(oUser.get()) || userName.equals("admin");
		if(!userInProject){
			return ResponseEntity.status(401).body("Only users in project can access tasks");
		}

		if(!task.getIsOpen() && !userName.equals(task.getProject().getLeader().getUsername())) {
			return ResponseEntity.status(401).body("Only the leader can assign non-open tasks to users");
		}
		if(task.getComplete() || user.getAssignedTasks().contains(task) ||
				!user.getSkills().containsAll(task.getRequiredSkills())) {
			return ResponseEntity.badRequest().build();
		}
		user.getAssignedTasks().add(task);
		task.getAssignees().add(user);
		userRepository.save(user);
		return ResponseEntity.ok(taskRepository.save(task));
	}
	
	@PutMapping("/{id}/unassign")
	public ResponseEntity unassignTask(@PathVariable Integer id,Authentication auth){
		String userName = auth.getName();
		Optional<Task> oTask = taskRepository.findById(id);
		Optional<User> oUser = userRepository.findByUsername(userName);
		if (!oUser.isPresent() || !oTask.isPresent()) {
            return ResponseEntity.notFound().build();
        }
		Task task = oTask.get();
		User user = oUser.get();
		Boolean userInProject = task.getProject().getMembers().contains(oUser.get()) || userName.equals("admin");
		if(!userInProject){
			return ResponseEntity.status(401).body("Only users in project can access tasks");
		}

		if(!task.getIsOpen() && !userName.equals(task.getProject().getLeader().getUsername())) {
			return ResponseEntity.status(401).body("Only the leader can unassign non-open tasks to users");
		}
		if(task.getComplete() || !user.getAssignedTasks().contains(task)) {
			return ResponseEntity.badRequest().build();
		}
		user.getAssignedTasks().remove(task);
		task.getAssignees().remove(user);
		userRepository.save(user);
		return ResponseEntity.ok(taskRepository.save(task));
	}
	
	@PutMapping("/{id}/complete")
    public ResponseEntity completeTask( @PathVariable Integer id,Authentication auth){
		 String userName = auth.getName();
    	 Optional<User> oUser = userRepository.findByUsername(userName);
    	 Optional<Task> oTask = taskRepository.findById(id);
         if (!oUser.isPresent() || !oTask.isPresent()) {
             return ResponseEntity.notFound().build();
         }
         User user = oUser.get();
         Task task = oTask.get();
 		 Boolean userInProject = task.getProject().getMembers().contains(oUser.get()) || userName.equals("admin");
 		 if(!userInProject){
			 return ResponseEntity.status(401).body("Only users in project can access tasks");
		 }
 		 if(!task.getAssignees().contains(user) && !userName.equals(task.getProject().getLeader().getUsername())){
 			 return ResponseEntity.status(401).body("Only users assigned to this task (or the project leader) can finish it");
 		 }
	     if(task.getStartTime() == null) {
	    	 return ResponseEntity.badRequest().body("Unstarted tasks cannot be finished");
	     }
	     for(User u : task.getAssignees()){
	    	 u.getAssignedTasks().remove(oTask.get());
	    	 userRepository.save(u);
	     }
	     task.setCompletedBy(user.getUsername());
	     task.setComplete(true);
	     task.setCompletionTime(LocalDateTime.now());
	     
	     return ResponseEntity.ok(taskRepository.save(task));
    }
    
    @PostMapping("/{id}/start")
    public ResponseEntity startTask(@PathVariable Integer id,Authentication auth){
    	 String userName = auth.getName();
    	 Optional<Task> oTask = taskRepository.findById(id);
         if (!oTask.isPresent()) {
             return ResponseEntity.notFound().build();
         }
         Task task = oTask.get();
         User user = userRepository.findByUsername(userName).get();
 		 Boolean userInProject = task.getProject().getMembers().contains(user) || userName.equals("admin");
 		 if(!userInProject){
			 return ResponseEntity.status(401).body("Only users in project can access tasks");
		 }
 		 if(!task.getAssignees().contains(user) && !userName.equals(task.getProject().getLeader().getUsername())){
 			 return ResponseEntity.status(401).body("Only users assigned to this task (or the project leader) can start it");
 		 }
         Boolean prerequisiteCheck = true;
         for(Task t :task.getPrerequisites()) {
        	 prerequisiteCheck = prerequisiteCheck && t.getComplete();
         }
         if(!prerequisiteCheck) {
        	 return ResponseEntity.badRequest().build();
         }
         task.setStartTime(LocalDateTime.now());
         return ResponseEntity.ok(taskRepository.save(task));

    }
}
