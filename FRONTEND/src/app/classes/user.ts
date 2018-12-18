import { Task } from 'src/app/classes/task';
import { Skill } from './skill';
import { Project } from 'src/app/classes/projects';

export class User {
  public id: number;
  public name: string;
  public username: string;
  public password: string;
  public projects: Project[];
  public ownedProjects: Project[];
  public skills: Skill[];
  public assignedTasks: Task[];

  constructor(name: string, userName: string, password: string) {
    this.name = name;
    this.username = userName;
    this.password = password;
    this.projects = [];
    this.ownedProjects = [];
    this.skills = [];
    this.assignedTasks = [];
  }
}
