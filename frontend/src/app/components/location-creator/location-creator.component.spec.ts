import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationCreatorComponent } from './location-creator.component';

describe('LocationCreatorComponent', () => {
  let component: LocationCreatorComponent;
  let fixture: ComponentFixture<LocationCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LocationCreatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
