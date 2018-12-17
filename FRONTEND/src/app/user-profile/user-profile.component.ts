import { Skill } from './../classes/skill';
import { AuthenticationService } from './../services/auth.service';
import { SkillService } from './../services/skill.service';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormControl, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { User } from '../classes/user';
import { UserService } from '../services/user.service';
import { ActivatedRoute } from '@angular/router';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent } from '@angular/material';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: [
    '../common-styles.scss',
    './user-profile.component.scss'
  ]
})
export class UserProfileComponent implements OnInit {

  private currentUser: User;

  private allSkills: Skill[] = [];
  private userSkills: Skill[] = [];

  private myControl = new FormControl();
  private filteredOptions: Observable<string[]>;
  private separatorKeysCodes: number[] = [ENTER, COMMA];
  private selectable = true;
  private removable = true;
  private addBlurOn = true;

  @ViewChild('chipInput') chipInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(
    private route: ActivatedRoute,
    private skillService: SkillService,
    private userService: UserService,
    private authService: AuthenticationService
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
    this.currentUser = new User('', '', '');
    this.currentUser = JSON.parse(JSON.stringify(this.authService.currentUser));
    this.allSkills = await this.skillService.getAllSkills();
    this.userSkills = await this.userService.getSkillsOfUser(this.currentUser.id);
  }

  private async add(event: MatChipInputEvent) {
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      let _skill = this.allSkills.find(item => item.name === value);

      if (_skill === undefined) {
        _skill = new Skill(value);
        _skill = await this.skillService.addNewSkill(_skill);
      }

      await this.userService.addSkill(this.currentUser.id, _skill);
      this.allSkills = await this.skillService.getAllSkills();
      this.userSkills.push(_skill);

      if (input) {
        input.value = '';
      }
    }
  }

  private async remove(skill: Skill) {
    await this.userService.removeSkill(this.currentUser.id, skill);
    this.userSkills = await this.userService.getSkillsOfUser(this.currentUser.id);
  }

  async selected(event: MatAutocompleteSelectedEvent) {
    const _skill = this.allSkills.find(item => item.name === event.option.viewValue);

    await this.userService.addSkill(this.currentUser.id, _skill);
    this.allSkills = await this.skillService.getAllSkills();
    this.userSkills.push(_skill);

    this.chipInput.nativeElement.value = '';
    // this.fruitCtrl.setValue(null);
  }

  private async saveUserData() {
    this.authService.currentUser = this.currentUser;
    const _user = this.currentUser;
    _user.skills = null;
    await this.userService.editUser(_user);
  }

  private async restoreUserData() {
    this.currentUser = await this.userService.getUser(this.currentUser.id);
  }
}
