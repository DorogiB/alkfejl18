import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './login-form/login-form.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogRegistrationComponent } from './dialogs/dialog-registration/dialog-registration.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { NavbarComponent } from './navbar/navbar.component';
// tslint:disable-next-line:max-line-length
import { MatToolbarModule, MatGridListModule, MatAutocompleteModule, MatChipsModule, MatIconModule, MatDatepickerModule, MatNativeDateModule, MatSelectModule, MatSnackBar, MatSnackBarModule, MatDividerModule, MatCheckboxModule } from '@angular/material';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { UserProjectsComponent } from './user-projects/user-projects.component';
import { ProjectTasksComponent } from './project-tasks/project-tasks.component';
import { ProjectMembersComponent } from './project-members/project-members.component';
import { DialogCreateProjectComponent } from './dialogs/dialog-create-project/dialog-create-project.component';
import { DialogAddMemberComponent } from './dialogs/dialog-add-member/dialog-add-member.component';
import { DialogAddTaskComponent } from './dialogs/dialog-add-task/dialog-add-task.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    UserProfileComponent,
    NavbarComponent,
    UserProjectsComponent,
    ProjectTasksComponent,
    ProjectMembersComponent,
    DialogRegistrationComponent,
    DialogCreateProjectComponent,
    DialogAddMemberComponent,
    DialogAddTaskComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    MatToolbarModule,
    MatGridListModule,
    MatAutocompleteModule,
    MatChipsModule,
    MatIconModule,
    MatDatepickerModule,
    FormsModule,
    MatSelectModule,
    MatSnackBarModule,
    BrowserModule,
    FormsModule,
    MatDividerModule,
    HttpClientModule,
    MatCheckboxModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    DialogRegistrationComponent,
    DialogCreateProjectComponent,
    DialogAddMemberComponent,
    DialogAddTaskComponent,
  ]
})
export class AppModule { }
