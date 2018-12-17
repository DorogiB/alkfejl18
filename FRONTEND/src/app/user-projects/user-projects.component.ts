import { AuthenticationService } from './../services/auth.service';
import { DialogCreateProjectComponent } from './../dialogs/dialog-create-project/dialog-create-project.component';
import { MatDialog } from '@angular/material';
import { Component, OnInit } from '@angular/core';
import { ProjectService } from './../services/project.service';
import { Project } from './../classes/projects';
import { User } from '../classes/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: [
    '../common-styles.scss',
    './user-projects.component.scss'
  ]
})
export class UserProjectsComponent implements OnInit {

  private projects: Project[];
  private ownProjects: Project[];
  public _project: Project;

  constructor(
    private projectService: ProjectService,
    private userService: UserService,
    private authService: AuthenticationService,
    private dialog: MatDialog
  ) { }

  async ngOnInit() {
    this.refreshLists();
  }

  private async refreshLists() {
    this.projects = await this.userService.getProjects(this.authService.currentUser.id);
    this.ownProjects = await this.userService.getOwnProjects(this.authService.currentUser.id);
    this.projects = this.projects.filter(project => !(this.ownProjects.map(p => p.id).includes(project.id)));
  }

  private createProject(): void {
    const dialogRef = this.dialog.open(DialogCreateProjectComponent, { width: '350px' });

    dialogRef.afterClosed().subscribe(async _name => {
      if (_name !== undefined) {
        const newProject = new Project(_name, this.authService.currentUser.id);
        console.log('NEW PROJECT:', newProject);
        await this.projectService.addNewProject(newProject);
        this.refreshLists();
      }
    });
  }
}
