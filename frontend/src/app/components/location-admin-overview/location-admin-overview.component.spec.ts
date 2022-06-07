import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationAdminOverviewComponent } from './location-admin-overview.component';

describe('LocationAdminOverviewComponent', () => {
  let component: LocationAdminOverviewComponent;
  let fixture: ComponentFixture<LocationAdminOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LocationAdminOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationAdminOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
