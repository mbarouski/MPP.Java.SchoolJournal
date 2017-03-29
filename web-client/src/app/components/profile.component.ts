import { Component } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'profile-component',
  templateUrl: './templates/profile.component.html',
  styleUrls: ['./styles/profile.component.css']
})
export class ProfileComponent {
  user: any = {
    name: "Максим Боровский"
  };
}
