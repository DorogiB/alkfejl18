import { MatDialogRef } from '@angular/material';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dialog-create-project',
  templateUrl: './dialog-create-project.component.html',
  styleUrls: [
    '../common-dialog-styles.scss',
    './dialog-create-project.component.scss'
  ]
})
export class DialogCreateProjectComponent implements OnInit {

  private projectName: string;

  constructor(private dialogRef: MatDialogRef<DialogCreateProjectComponent>) { }

  ngOnInit() { }
}
