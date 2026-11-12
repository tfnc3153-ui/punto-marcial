import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarClienteComponent } from './registrar-cliente';

describe('RegistrarClienteComponent', () => {

  let component: RegistrarClienteComponent;
  let fixture: ComponentFixture<RegistrarClienteComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [RegistrarClienteComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrarClienteComponent);
    component = fixture.componentInstance;

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});