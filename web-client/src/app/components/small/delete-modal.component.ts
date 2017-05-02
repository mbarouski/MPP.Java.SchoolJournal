import {Component, Input, OnChanges, ViewChild} from '@angular/core';
import {ModalDirective} from "ng2-bootstrap";

@Component({
  moduleId: module.id,
  selector: 'delete-modal',
  templateUrl: './templates/delete-modal.component.html',
})
export class DeleteModalComponent implements OnChanges{
  @Input() yesCallback;
  @Input() noCallback;
  @Input() isActive;
  @ViewChild('deleteModal') public deleteModal: ModalDirective;

  ngOnChanges() {
    this.isActive ? this.deleteModal.show() : this.deleteModal.hide();
  }

  handleYes() {
    this.deleteModal.hide();
    this.yesCallback()
  }
}
