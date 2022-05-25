import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSearchResultComponent } from './show-search-result.component';

describe('ShowSearchResultComponent', () => {
  let component: ShowSearchResultComponent;
  let fixture: ComponentFixture<ShowSearchResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowSearchResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
