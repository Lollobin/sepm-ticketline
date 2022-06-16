import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailedViewComponent } from './article-detailed-view.component';

describe('ArticleDetailedViewComponent', () => {
  let component: ArticleDetailedViewComponent;
  let fixture: ComponentFixture<ArticleDetailedViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticleDetailedViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailedViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
