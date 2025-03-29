import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../kyc-data-request.test-samples';

import { KycDataRequestFormService } from './kyc-data-request-form.service';

describe('KycDataRequest Form Service', () => {
  let service: KycDataRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KycDataRequestFormService);
  });

  describe('Service methods', () => {
    describe('createKycDataRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createKycDataRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requestRef: expect.any(Object),
            requestDate: expect.any(Object),
            status: expect.any(Object),
            requestPurpose: expect.any(Object),
            encryptedResponseData: expect.any(Object),
            respondedAt: expect.any(Object),
            customer: expect.any(Object),
            fromBank: expect.any(Object),
            toBank: expect.any(Object),
            consent: expect.any(Object),
          }),
        );
      });

      it('passing IKycDataRequest should create a new form with FormGroup', () => {
        const formGroup = service.createKycDataRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requestRef: expect.any(Object),
            requestDate: expect.any(Object),
            status: expect.any(Object),
            requestPurpose: expect.any(Object),
            encryptedResponseData: expect.any(Object),
            respondedAt: expect.any(Object),
            customer: expect.any(Object),
            fromBank: expect.any(Object),
            toBank: expect.any(Object),
            consent: expect.any(Object),
          }),
        );
      });
    });

    describe('getKycDataRequest', () => {
      it('should return NewKycDataRequest for default KycDataRequest initial value', () => {
        const formGroup = service.createKycDataRequestFormGroup(sampleWithNewData);

        const kycDataRequest = service.getKycDataRequest(formGroup) as any;

        expect(kycDataRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewKycDataRequest for empty KycDataRequest initial value', () => {
        const formGroup = service.createKycDataRequestFormGroup();

        const kycDataRequest = service.getKycDataRequest(formGroup) as any;

        expect(kycDataRequest).toMatchObject({});
      });

      it('should return IKycDataRequest', () => {
        const formGroup = service.createKycDataRequestFormGroup(sampleWithRequiredData);

        const kycDataRequest = service.getKycDataRequest(formGroup) as any;

        expect(kycDataRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IKycDataRequest should not enable id FormControl', () => {
        const formGroup = service.createKycDataRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewKycDataRequest should disable id FormControl', () => {
        const formGroup = service.createKycDataRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
