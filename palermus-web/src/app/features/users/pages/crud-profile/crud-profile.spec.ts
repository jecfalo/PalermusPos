import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudProfile } from './crud-profile';

describe('CrudProfile', () => {
  let component: CrudProfile;
  let fixture: ComponentFixture<CrudProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrudProfile],
    }).compileComponents();

    fixture = TestBed.createComponent(CrudProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
