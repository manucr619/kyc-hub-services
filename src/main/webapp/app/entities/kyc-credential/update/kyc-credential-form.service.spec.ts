import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-credential.test-samples';

import { KycCredentialFormService } from './kyc-credential-form.service';

describe('KycCredential Form Service', () => {
  let service: KycCredentialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KycCredentialFormService);
  });

  describe('Service methods', () => {
    describe('createKycCredentialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycCredentialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issuedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            status: expect.any(Object),
            levelOfVerification: expect.any(Object),
            schemaVersion: expect.any(Object),
            credentialHash: expect.any(Object),
            signature: expect.any(Object),
            issuer: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IKycCredential should create a new form with FormGroup', () => {
        const formGroup = service.createKycCredentialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            issuedDate: expect.any(Object),
            expiryDate: expect.any(Object),
            status: expect.any(Object),
            levelOfVerification: expect.any(Object),
            schemaVersion: expect.any(Object),
            credentialHash: expect.any(Object),
            signature: expect.any(Object),
            issuer: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycCredential', () => {
      it('should return NewKycCredential for default KycCredential initial value', () => {
        const formGroup = service.createKycCredentialFormGroup(sampleWithNewData);

        const kycCredential = service.getKycCredential(formGroup) as any;

        expect(kycCredential).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycCredential for empty KycCredential initial value', () => {
        const formGroup = service.createKycCredentialFormGroup();

        const kycCredential = service.getKycCredential(formGroup) as any;

        expect(kycCredential).toMatchObject({});
      });

      it('should return IKycCredential', () => {
        const formGroup = service.createKycCredentialFormGroup(sampleWithRequiredData);

        const kycCredential = service.getKycCredential(formGroup) as any;

        expect(kycCredential).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycCredential should not enable id FormControl', () => {
        const formGroup = service.createKycCredentialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycCredential should disable id FormControl', () => {
        const formGroup = service.createKycCredentialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
