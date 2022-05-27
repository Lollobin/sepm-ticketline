import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationSeatingPlansComponent } from './location-seating-plans.component';

describe('LocationSeatingPlansComponent', () => {
  let component: LocationSeatingPlansComponent;
  let fixture: ComponentFixture<LocationSeatingPlansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LocationSeatingPlansComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationSeatingPlansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
