import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-consent.test-samples';

import { KycConsentFormService } from './kyc-consent-form.service';

describe('KycConsent Form Service', () => {
  let service: KycConsentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KycConsentFormService);
  });

  describe('Service methods', () => {
    describe('createKycConsentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycConsentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            consentToken: expect.any(Object),
            consentGivenAt: expect.any(Object),
            expiresAt: expect.any(Object),
            isRevoked: expect.any(Object),
            revokedAt: expect.any(Object),
            customer: expect.any(Object),
            issuerBank: expect.any(Object),
            recipientBank: expect.any(Object),
          }),
        );
      });

      it('passing IKycConsent should create a new form with FormGroup', () => {
        const formGroup = service.createKycConsentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            consentToken: expect.any(Object),
            consentGivenAt: expect.any(Object),
            expiresAt: expect.any(Object),
            isRevoked: expect.any(Object),
            revokedAt: expect.any(Object),
            customer: expect.any(Object),
            issuerBank: expect.any(Object),
            recipientBank: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycConsent', () => {
      it('should return NewKycConsent for default KycConsent initial value', () => {
        const formGroup = service.createKycConsentFormGroup(sampleWithNewData);

        const kycConsent = service.getKycConsent(formGroup) as any;

        expect(kycConsent).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycConsent for empty KycConsent initial value', () => {
        const formGroup = service.createKycConsentFormGroup();

        const kycConsent = service.getKycConsent(formGroup) as any;

        expect(kycConsent).toMatchObject({});
      });

      it('should return IKycConsent', () => {
        const formGroup = service.createKycConsentFormGroup(sampleWithRequiredData);

        const kycConsent = service.getKycConsent(formGroup) as any;

        expect(kycConsent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycConsent should not enable id FormControl', () => {
        const formGroup = service.createKycConsentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycConsent should disable id FormControl', () => {
        const formGroup = service.createKycConsentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
