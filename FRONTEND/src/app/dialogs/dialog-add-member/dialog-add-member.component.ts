import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import { User } from './../../classes/user';
import { Project } from './../../classes/projects';
import { ProjectService } from './../../services/project.service';
import { UserService } from './../../services/user.service';

@Component({
  selector: 'app-dialog-add-member',
  templateUrl: './dialog-add-member.component.html',
  styleUrls: [
    '../common-dialog-styles.scss',
    './dialog-add-member.component.scss'
  ]
})
export class DialogAddMemberComponent implements OnInit {

  private assignedUsers: User[];
  private users: User[];
  private selectedUser: User;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private projectService: ProjectService,
    @Inject(MAT_DIALOG_DATA) public data: Project
  ) { }

  async ngOnInit() {
    console.log(this.data);
    this.users = await this.userService.getUsers();
    this.assignedUsers = await this.projectService.getMembers(this.data.id);
    this.users = this.users.filter(user => !(this.assignedUsers.map(aUser => aUser.id).includes(user.id)));
  }
}
