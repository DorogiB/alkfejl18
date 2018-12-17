package hu.elte.alkfejl.alkfejl18.entities;

import java.sql.Date;
import java.util.ArrayList;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageWrapper {
	private String name;
	private Integer leaderId;
	private ArrayList<Integer> requiredSkills;
	private ArrayList<Integer> prerequisites;
	private Boolean isOpen;
	private Integer projectId;
	private String username;
	private String password;
	private Date deadline;
	
	public Boolean isProject() {
		return this.name != null && this.leaderId != null;
	}
	
	public Boolean isUser() {
		return this.username != null && this.password != null;
	}
	
	public Boolean isTask() {
		return this.projectId != null && this.name != null;
	}
}
