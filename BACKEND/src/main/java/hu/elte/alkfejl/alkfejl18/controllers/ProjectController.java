package hu.elte.alkfejl.alkfejl18.controllers;

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
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    
    @GetMapping("")
    public ResponseEntity<Iterable<Project>> getAll() {
        Iterable<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }
     
    @PostMapping("/new")
    public ResponseEntity<Project> createProject(@RequestBody MessageWrapper project) {
    	if(!project.isProject()) {
    		return ResponseEntity.badRequest().build();
    	}
        Optional<User> oLeader = userRepository.findById(project.getLeaderId());
    	if (!oLeader.isPresent()) {
    		return ResponseEntity.badRequest().build();
    	}
    	Project newProject = new Project(null,oLeader.get(),new ArrayList<User>(),new ArrayList<Task>(), null,project.getName());
    	newProject.getMembers().add(oLeader.get());
    	oLeader.get().getOwnedProjects().add(newProject);
    	userRepository.save(oLeader.get());
        return ResponseEntity.ok(projectRepository.save(newProject));
    }
        
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(project.get());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable Integer id,Authentication auth) {
    	String userName = auth.getName();
        Optional<Project> oProject = projectRepository.findById(id);
        if (!oProject.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        Project project = oProject.get();
        if(!userName.equals("admin") || !userName.equals(project.getLeader().getUsername())) {
        	return ResponseEntity.status(401).body("Only the leader can delete the project");
        }
        for(Task t : project.getTasks()) {
        	for(User u : t.getAssignees()) {
        		u.getAssignedTasks().remove(t);
        	}
        	taskRepository.delete(t);
        }
        for(User u : project.getMembers()) {
        	u.getProjects().remove(project);
        	userRepository.delete(u);
        }
        Optional<User> leader = userRepository.findById(project.getLeader().getId());
        leader.get().getOwnedProjects().remove(project);
        userRepository.save(leader.get());
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }
     
    @PutMapping("/edit/{id}")
    public ResponseEntity editProject(@PathVariable Integer id, @RequestBody MessageWrapper project,Authentication auth) {
    	String userName = auth.getName();
    	if(!project.isProject()) {
    		return ResponseEntity.badRequest().build();
    	}
        Optional<Project> oProject = projectRepository.findById(id);
        if (!oProject.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Project editedProject = oProject.get();
        if(!userName.equals("admin") || !userName.equals(editedProject.getLeader().getUsername())) {
        	return ResponseEntity.status(401).body("Only the project leader can edit it");
        }
        if(project.getName() != null) {
        	editedProject.setName(project.getName());
        }
        if(project.getDeadline() != null) {
        	editedProject.setDeadline(project.getDeadline());
        }
        return ResponseEntity.ok(projectRepository.save(editedProject));
    }
    
    @GetMapping("/{id}/members")
    public ResponseEntity getMembers(@PathVariable Integer id,Authentication auth){
    	String userName = auth.getName();
    	Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        if(!userName.equals("admin")&&!project.get().getMembers().contains(userRepository.findByUsername(userName).get())) {
        	return ResponseEntity.status(401).body("Only members of the project can see the list of members");
        }
        return ResponseEntity.ok(project.get().getMembers());
    }
    
    @PostMapping("/{id}/addMember")
    public ResponseEntity addMember(@PathVariable Integer id,@RequestBody MessageWrapper member,Authentication auth){
    	String userName = auth.getName();
    	Optional<Project> oProject = projectRepository.findById(id);
    	Optional<User> oMember = userRepository.findByUsername(member.getUsername());
    	if(!oProject.isPresent() || !oMember.isPresent()) {
    		return ResponseEntity.notFound().build();
    	}
    	Project project = oProject.get();
    	if(!userName.equals("admin") && !userName.equals(project.getLeader().getUsername())) {
    		return ResponseEntity.status(401).body("Only the project leader can add members");
    	}
    	if(project.getMembers().contains(oMember.get())) {
    		return ResponseEntity.badRequest().build();
    	}
    	oMember.get().getProjects().add(project);
    	project.getMembers().add(oMember.get());
    	userRepository.save(oMember.get());
    	projectRepository.save(project);
    	return ResponseEntity.ok(project.getMembers());
    }
    
    @PostMapping("/{id}/removeMember/{userId}")
    public ResponseEntity removeMember(@PathVariable Integer id, @RequestBody MessageWrapper member,Authentication auth){
    	String userName = auth.getName();
    	Optional<Project> oProject = projectRepository.findById(id);
    	Optional<User> oMember = userRepository.findByUsername(member.getUsername());
    	if(!oProject.isPresent() || !oMember.isPresent() || !oProject.get().getMembers().contains(oMember.get())) {
    		return ResponseEntity.notFound().build();
    	}
    	if(!userName.equals("admin") && !userName.equals(oProject.get().getLeader().getUsername())){
    		return ResponseEntity.status(401).body("Only the project leader can remove members");
    	}
    	return ResponseEntity.ok().build();
    }
}
