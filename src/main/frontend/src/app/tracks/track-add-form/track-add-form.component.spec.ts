import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackAddFormComponent } from './track-add-form.component';

describe('TrackAddFormComponent', () => {
  let component: TrackAddFormComponent;
  let fixture: ComponentFixture<TrackAddFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrackAddFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackAddFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
