import { Component } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'menu-component',
  templateUrl: './templates/menu.component.html',
  styleUrls: ['./styles/menu.component.css']
})
export class MenuComponent {
  isCollapsed: Boolean = true;
}
