import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { KycConsentDetailComponent } from './kyc-consent-detail.component';

describe('KycConsent Management Detail Component', () => {
  let comp: KycConsentDetailComponent;
  let fixture: ComponentFixture<KycConsentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KycConsentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./kyc-consent-detail.component').then(m => m.KycConsentDetailComponent),
              resolve: { kycConsent: () => of({ id: 12952 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(KycConsentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KycConsentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kycConsent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', KycConsentDetailComponent);

      // THEN
      expect(instance.kycConsent()).toEqual(expect.objectContaining({ id: 12952 }));
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
