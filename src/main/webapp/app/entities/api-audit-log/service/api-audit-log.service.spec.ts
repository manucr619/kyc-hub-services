import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAPIAuditLog } from '../api-audit-log.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../api-audit-log.test-samples';

import { APIAuditLogService, RestAPIAuditLog } from './api-audit-log.service';

const requireRestSample: RestAPIAuditLog = {
  ...sampleWithRequiredData,
  eventTime: sampleWithRequiredData.eventTime?.toJSON(),
};

describe('APIAuditLog Service', () => {
  let service: APIAuditLogService;
  let httpMock: HttpTestingController;
  let expectedResult: IAPIAuditLog | IAPIAuditLog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(APIAuditLogService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a APIAuditLog', () => {
      const aPIAuditLog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aPIAuditLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APIAuditLog', () => {
      const aPIAuditLog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aPIAuditLog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a APIAuditLog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APIAuditLog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a APIAuditLog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAPIAuditLogToCollectionIfMissing', () => {
      it('should add a APIAuditLog to an empty array', () => {
        const aPIAuditLog: IAPIAuditLog = sampleWithRequiredData;
        expectedResult = service.addAPIAuditLogToCollectionIfMissing([], aPIAuditLog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPIAuditLog);
      });

      it('should not add a APIAuditLog to an array that contains it', () => {
        const aPIAuditLog: IAPIAuditLog = sampleWithRequiredData;
        const aPIAuditLogCollection: IAPIAuditLog[] = [
          {
            ...aPIAuditLog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAPIAuditLogToCollectionIfMissing(aPIAuditLogCollection, aPIAuditLog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APIAuditLog to an array that doesn't contain it", () => {
        const aPIAuditLog: IAPIAuditLog = sampleWithRequiredData;
        const aPIAuditLogCollection: IAPIAuditLog[] = [sampleWithPartialData];
        expectedResult = service.addAPIAuditLogToCollectionIfMissing(aPIAuditLogCollection, aPIAuditLog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPIAuditLog);
      });

      it('should add only unique APIAuditLog to an array', () => {
        const aPIAuditLogArray: IAPIAuditLog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aPIAuditLogCollection: IAPIAuditLog[] = [sampleWithRequiredData];
        expectedResult = service.addAPIAuditLogToCollectionIfMissing(aPIAuditLogCollection, ...aPIAuditLogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPIAuditLog: IAPIAuditLog = sampleWithRequiredData;
        const aPIAuditLog2: IAPIAuditLog = sampleWithPartialData;
        expectedResult = service.addAPIAuditLogToCollectionIfMissing([], aPIAuditLog, aPIAuditLog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPIAuditLog);
        expect(expectedResult).toContain(aPIAuditLog2);
      });

      it('should accept null and undefined values', () => {
        const aPIAuditLog: IAPIAuditLog = sampleWithRequiredData;
        expectedResult = service.addAPIAuditLogToCollectionIfMissing([], null, aPIAuditLog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPIAuditLog);
      });

      it('should return initial array if no APIAuditLog is added', () => {
        const aPIAuditLogCollection: IAPIAuditLog[] = [sampleWithRequiredData];
        expectedResult = service.addAPIAuditLogToCollectionIfMissing(aPIAuditLogCollection, undefined, null);
        expect(expectedResult).toEqual(aPIAuditLogCollection);
      });
    });

    describe('compareAPIAuditLog', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAPIAuditLog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 23832 };
        const entity2 = null;

        const compareResult1 = service.compareAPIAuditLog(entity1, entity2);
        const compareResult2 = service.compareAPIAuditLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 23832 };
        const entity2 = { id: 18935 };

        const compareResult1 = service.compareAPIAuditLog(entity1, entity2);
        const compareResult2 = service.compareAPIAuditLog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 23832 };
        const entity2 = { id: 23832 };

        const compareResult1 = service.compareAPIAuditLog(entity1, entity2);
        const compareResult2 = service.compareAPIAuditLog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
