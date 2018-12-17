import { startWith, map } from 'rxjs/operators';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { SkillService } from './../../services/skill.service';
import { Skill } from './../../classes/skill';
import { TaskService } from './../../services/task.service';
import { MAT_DIALOG_DATA, MatAutocomplete, MatChipInputEvent } from '@angular/material';
import { Component, OnInit, Inject, ViewChild, ElementRef } from '@angular/core';
import { Task } from 'src/app/classes/task';
import { Project } from 'src/app/classes/projects';
import { ProjectService } from 'src/app/services/project.service';

@Component({
  selector: 'app-dialog-add-task',
  templateUrl: './dialog-add-task.component.html',
  styleUrls: [
    '../common-dialog-styles.scss',
    './dialog-add-task.component.css'
  ]
})
export class DialogAddTaskComponent implements OnInit {

  private projectTasks: Task[];
  private _task: Task;
  private allSkills: Skill[] = [];

  private myControl = new FormControl();
  private filteredOptions: Observable<string[]>;
  private separatorKeysCodes: number[] = [ENTER, COMMA];
  private selectable = true;
  private removable = true;
  private addBlurOn = true;

  @ViewChild('chipInput') chipInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(
    private skillService: SkillService,
    private projectService: ProjectService,
    private taskService: TaskService,
    @Inject(MAT_DIALOG_DATA) public data: { project: Project, tasks: Task[] }
  ) {
    this.filteredOptions = this.myControl.valueChanges.pipe(
      startWith(null),
      map((src: string | null) => src ? this._filter(src) : this.allSkills.map(skill => skill.name))
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.allSkills.filter(option => option.name.toLowerCase().indexOf(filterValue) === 0).map(skill => skill.name);
  }

  async ngOnInit() {
    this._task = new Task();
    this._task.projectId = this.data.project.id;
    const allTasks: Task[] = await this.taskService.getTasks();
    this.projectTasks = allTasks.filter(task => task.project.id === this.data.project.id);
    this.allSkills = await this.skillService.getAllSkills();

    console.log('Dialog (all tasks):', allTasks);
    console.log('Dialog (project tasks):', this.projectTasks);
  }

  private async addSkill(event: MatChipInputEvent) {
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      let _skill = this.allSkills.find(item => item.name === value);

      if (_skill === undefined) {
        _skill = new Skill(value);
        _skill = await this.skillService.addNewSkill(_skill);
      }

      this._task.requiredSkills.push(_skill.id);
    }
    console.log(this._task);
  }

  private async removeSkill(skillToRemove: Skill) {
    this._task.requiredSkills = this._task.requiredSkills.filter(skill => skill !== skillToRemove.id);
  }
}
