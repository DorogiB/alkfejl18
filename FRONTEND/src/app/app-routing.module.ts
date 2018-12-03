import { ProjectTasksComponent } from './project-tasks/project-tasks.component';
import { UserProjectsComponent } from './user-projects/user-projects.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ProjectMembersComponent } from './project-members/project-members.component';

const routes: Routes = [
  { path: '', component: LoginFormComponent },
  { path: 'users/:uid/projects', component: UserProjectsComponent },
  { path: 'users/:uid/profile', component: UserProfileComponent },
  { path: 'projects/:pid/tasks', component: ProjectTasksComponent },
  { path: 'projects/:pid/members', component: ProjectMembersComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
