package hu.elte.alkfejl.alkfejl18.controllers;

import java.util.ArrayList;
import java.util.Optional;
import hu.elte.alkfejl.alkfejl18.entities.*;
import hu.elte.alkfejl.alkfejl18.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    
    @GetMapping("")
    public ResponseEntity getAll(Authentication auth) {
    	String userName = auth.getName();
    	//if(userName.equals("admin")) {
        Iterable<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    	//}else {
    	//	return ResponseEntity.status(401).body("Only the admin has access to the list of all users");
    	//}
    }
     
    @PostMapping("/new")
    public ResponseEntity<User> createUser(@RequestBody MessageWrapper user) {
        if(!user.isUser()) {
        	return ResponseEntity.badRequest().build();
        }
        User newUser = new User(null,user.getUsername(),passwordEncoder.encode(user.getPassword()),user.getName(),new ArrayList<Project>(),new ArrayList<Project>(),
        		new ArrayList<Skill>(),new ArrayList<Task>());
        return ResponseEntity.ok(userRepository.save(newUser));
    }
    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody String username) {
        Optional<User> oUser = userRepository.findByUsername(username);
        if (!oUser.isPresent()) {
            return ResponseEntity.status(401).body("User not found");
        }
        return ResponseEntity.ok(oUser.get());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        
        return ResponseEntity.ok(oUser.get());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id,Authentication auth) {
    	String userName = auth.getName();
        Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        if(userName.equals("admin") || userName.equals(oUser.get().getUsername())) {
        	if(oUser.get().getOwnedProjects().size() > 0 || oUser.get().getProjects().size() > 0 ||
            		oUser.get().getAssignedTasks().size() > 0) {
            	return ResponseEntity.badRequest().body("User can't be deleted, since it is in a project");
            }
            userRepository.delete(oUser.get());
            return ResponseEntity.ok().build();
        }else {
        	return ResponseEntity.status(401).body("Only the owner can delete the user");
        }
        
    }
    
    @PutMapping("/{id}/edit")
    public ResponseEntity editUser(@PathVariable Integer id,@RequestBody MessageWrapper user, Authentication auth){
    	String userName = auth.getName();
    	if(!user.isUser()) {
    		return ResponseEntity.badRequest().build();
    	}
    	Optional<User> oUser = userRepository.findById(id);
        if (!oUser.isPresent()) {
            return ResponseEntity.notFound().build();   
        }
        User editedUser = oUser.get(); 
        if(userName.equals("admin") || userName.equals(editedUser.getUsername())) {
        	if(user.getName() != null) {
        		editedUser.setName(user.getName());
        	}
        	if(user.getUsername() != null) {
        		editedUser.setUsername(user.getUsername());
        	}
        	if(user.getPassword() != null) {
        		editedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        	}
        	return ResponseEntity.ok(userRepository.save(editedUser));
        }else {
        	return ResponseEntity.status(401).body("Only the owner can edit the user");
        }
        
    }
    
    @GetMapping("/{id}/skills")
    public ResponseEntity<Iterable<Skill>> getSkillList(@PathVariable Integer id){
    	 Optional<User> oUser = userRepository.findById(id);
         if (!oUser.isPresent()) {
             return ResponseEntity.notFound().build();
         }

         return ResponseEntity.ok(oUser.get().getSkills());
    }
    
    @PutMapping("/{id}/skills/add")
    public ResponseEntity addSkill(@PathVariable Integer id, @RequestBody MessageWrapper skill,Authentication auth) {
        String userName = auth.getName();
    	Optional<User> oUser = userRepository.findById(id);
        Optional<Skill> oSkill = skillRepository.findByName(skill.getName());
        if (!oUser.isPresent() || !oSkill.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if(userName.equals("admin") || userName.equals(oUser.get().getUsername())) {
	        if(oUser.get().getSkills().contains(oSkill.get())) {
	        	return ResponseEntity.ok(oUser.get());
	        }
	        Skill addedSkill = oSkill.get();
	        User user = oUser.get();
	        user.getSkills().add(addedSkill);
	        addedSkill.getOwners().add(user);
	        skillRepository.save(addedSkill);
	        return ResponseEntity.ok(userRepository.save(user));
        }else {
        	return ResponseEntity.status(401).body("Only the owner can add skills to a user");
        }
    }
    
    @PutMapping("/{id}/skills/remove")
    public ResponseEntity removeSkill(@PathVariable Integer id, @RequestBody MessageWrapper skill,Authentication auth) {
    	String userName = auth.getName();
    	Optional<User> oUser = userRepository.findById(id);
        Optional<Skill> oSkill = skillRepository.findByName(skill.getName());
        if (!oUser.isPresent() || !oSkill.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if(userName.equals("admin") || userName.equals(oUser.get().getUsername())) {
	        if(!oUser.get().getSkills().contains(oSkill.get())) {
	        	return ResponseEntity.ok(oUser.get());
	        }
	        oUser.get().getSkills().remove(oSkill.get());
	        oSkill.get().getOwners().remove(oUser.get());
	        skillRepository.save(oSkill.get());
	        return ResponseEntity.ok(userRepository.save(oUser.get()));
        }else {
        	return ResponseEntity.status(401).body("Only the owner can add skills to a user");
        }
    }
    
    @GetMapping("/{id}/ownedProjects")
    public ResponseEntity<Iterable<Project>> getOwnedProjectList(@PathVariable Integer id){
    	 Optional<User> oUser = userRepository.findById(id);
         if (!oUser.isPresent()) {
             return ResponseEntity.notFound().build();
         }

         return ResponseEntity.ok(oUser.get().getOwnedProjects());
    }
    
    @GetMapping("/{id}/projects")
    public ResponseEntity<Iterable<Project>> getProjectList(@PathVariable Integer id){
    	 Optional<User> oUser = userRepository.findById(id);
         if (!oUser.isPresent()) {
             return ResponseEntity.notFound().build();
         }

         return ResponseEntity.ok(oUser.get().getProjects());
    }
    
    @GetMapping("/{id}/assignedTasks")
    public ResponseEntity<Iterable<Task>> getTaskList(@PathVariable Integer id){
    	 Optional<User> oUser = userRepository.findById(id);
         if (!oUser.isPresent()) {
             return ResponseEntity.notFound().build();
         }

         return ResponseEntity.ok(oUser.get().getAssignedTasks());
    }
    
    
    
}
