import { DialogAddMemberComponent } from './../dialogs/dialog-add-member/dialog-add-member.component';
import { MatDialog } from '@angular/material';
import { ProjectService } from './../services/project.service';
import { User } from './../classes/user';
import { UserService } from './../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, Input } from '@angular/core';
import { Project } from '../classes/projects';
import { select } from 'd3';

@Component({
  selector: 'app-project-members',
  templateUrl: './project-members.component.html',
  styleUrls: [
    '../common-styles.scss',
    './project-members.component.scss'
  ]
})
export class ProjectMembersComponent implements OnInit {

  public project: Project = new Project('', 0);
  private assignedUsers: User[];

  private selectedUser: User;
  private userOwnProjects: Project[];
  private userProjects: Project[];

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private projectService: ProjectService,
    public dialog: MatDialog
  ) { }

  async ngOnInit() {
    const projectId: number = parseInt(this.route.snapshot.paramMap.get('pid'), 10);
    this.project = await this.projectService.getProject(projectId);
  }

  private openAddMemberDialog(): void {
    const dialogRef = this.dialog.open(DialogAddMemberComponent, {
      width: '350px',
      data: this.project
    });

    dialogRef.afterClosed().subscribe(async selectedUser => {
      console.log(selectedUser);

      if (!selectedUser) { return; }
      console.log(await this.projectService.addMember(this.project.id, selectedUser));
    });
  }

  private selectUser(user: User): void {
    // this.selectedUser = user;
    // this.projectService.getUserProjects(user.id).subscribe(projects => this.userProjects = projects);
    // this.projectService.getUserOwnProjects(user.id).subscribe(projects => this.userOwnProjects = projects);
  }

  private removeUser(): void {
    // this.projectService.removeUserFromProject(this.selectedUser.id, this.project.id);
    // this.userService.removeUserFromProject(this.selectedUser.id, this.project.id);
    // this.userService.getUsersByPID(this.project.id).subscribe(users => this.assignedUsers = users);
    // this.selectedUser = null;
  }
}
