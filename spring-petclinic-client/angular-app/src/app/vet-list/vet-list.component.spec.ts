import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VetListComponent } from './vet-list.component';

describe('VetListComponent', () => {
  let component: VetListComponent;
  let fixture: ComponentFixture<VetListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VetListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(VetListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
