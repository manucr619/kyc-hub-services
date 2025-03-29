import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { KycDataRequestDetailComponent } from './kyc-data-request-detail.component';

describe('KycDataRequest Management Detail Component', () => {
  let comp: KycDataRequestDetailComponent;
  let fixture: ComponentFixture<KycDataRequestDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KycDataRequestDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./kyc-data-request-detail.component').then(m => m.KycDataRequestDetailComponent),
              resolve: { kycDataRequest: () => of({ id: 32543 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(KycDataRequestDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KycDataRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kycDataRequest on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', KycDataRequestDetailComponent);

      // THEN
      expect(instance.kycDataRequest()).toEqual(expect.objectContaining({ id: 32543 }));
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
