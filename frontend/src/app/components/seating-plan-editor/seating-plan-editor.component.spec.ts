import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatingPlanEditorComponent } from './seating-plan-editor.component';

describe('SeatingPlanEditorComponent', () => {
  let component: SeatingPlanEditorComponent;
  let fixture: ComponentFixture<SeatingPlanEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SeatingPlanEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatingPlanEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
