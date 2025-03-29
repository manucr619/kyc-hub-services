import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { KycCredentialDetailComponent } from './kyc-credential-detail.component';

describe('KycCredential Management Detail Component', () => {
  let comp: KycCredentialDetailComponent;
  let fixture: ComponentFixture<KycCredentialDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KycCredentialDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./kyc-credential-detail.component').then(m => m.KycCredentialDetailComponent),
              resolve: { kycCredential: () => of({ id: 24791 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(KycCredentialDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KycCredentialDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load kycCredential on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', KycCredentialDetailComponent);

      // THEN
      expect(instance.kycCredential()).toEqual(expect.objectContaining({ id: 24791 }));
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
