import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../api-audit-log.test-samples';

import { APIAuditLogFormService } from './api-audit-log-form.service';

describe('APIAuditLog Form Service', () => {
  let service: APIAuditLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(APIAuditLogFormService);
  });

  describe('Service methods', () => {
    describe('createAPIAuditLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAPIAuditLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventTime: expect.any(Object),
            action: expect.any(Object),
            statusCode: expect.any(Object),
            message: expect.any(Object),
            sourceIP: expect.any(Object),
            initiatedBy: expect.any(Object),
            endpointAccessed: expect.any(Object),
            bank: expect.any(Object),
          }),
        );
      });

      it('passing IAPIAuditLog should create a new form with FormGroup', () => {
        const formGroup = service.createAPIAuditLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventTime: expect.any(Object),
            action: expect.any(Object),
            statusCode: expect.any(Object),
            message: expect.any(Object),
            sourceIP: expect.any(Object),
            initiatedBy: expect.any(Object),
            endpointAccessed: expect.any(Object),
            bank: expect.any(Object),
          }),
        );
      });
    });

    describe('getAPIAuditLog', () => {
      it('should return NewAPIAuditLog for default APIAuditLog initial value', () => {
        const formGroup = service.createAPIAuditLogFormGroup(sampleWithNewData);

        const aPIAuditLog = service.getAPIAuditLog(formGroup) as any;

        expect(aPIAuditLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewAPIAuditLog for empty APIAuditLog initial value', () => {
        const formGroup = service.createAPIAuditLogFormGroup();

        const aPIAuditLog = service.getAPIAuditLog(formGroup) as any;

        expect(aPIAuditLog).toMatchObject({});
      });

      it('should return IAPIAuditLog', () => {
        const formGroup = service.createAPIAuditLogFormGroup(sampleWithRequiredData);

        const aPIAuditLog = service.getAPIAuditLog(formGroup) as any;

        expect(aPIAuditLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAPIAuditLog should not enable id FormControl', () => {
        const formGroup = service.createAPIAuditLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAPIAuditLog should disable id FormControl', () => {
        const formGroup = service.createAPIAuditLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
