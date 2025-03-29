import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BankDetailComponent } from './bank-detail.component';

describe('Bank Management Detail Component', () => {
  let comp: BankDetailComponent;
  let fixture: ComponentFixture<BankDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BankDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bank-detail.component').then(m => m.BankDetailComponent),
              resolve: { bank: () => of({ id: 16728 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BankDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BankDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bank on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BankDetailComponent);

      // THEN
      expect(instance.bank()).toEqual(expect.objectContaining({ id: 16728 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
