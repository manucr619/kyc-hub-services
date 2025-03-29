import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IKycDataRequest } from '../kyc-data-request.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-data-request.test-samples';

import { KycDataRequestService, RestKycDataRequest } from './kyc-data-request.service';

const requireRestSample: RestKycDataRequest = {
  ...sampleWithRequiredData,
  requestDate: sampleWithRequiredData.requestDate?.toJSON(),
  respondedAt: sampleWithRequiredData.respondedAt?.toJSON(),
};

describe('KycDataRequest Service', () => {
  let service: KycDataRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycDataRequest | IKycDataRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycDataRequestService);
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

    it('should create a KycDataRequest', () => {
      const kycDataRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycDataRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycDataRequest', () => {
      const kycDataRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycDataRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycDataRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycDataRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycDataRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycDataRequestToCollectionIfMissing', () => {
      it('should add a KycDataRequest to an empty array', () => {
        const kycDataRequest: IKycDataRequest = sampleWithRequiredData;
        expectedResult = service.addKycDataRequestToCollectionIfMissing([], kycDataRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kycDataRequest);
      });

      it('should not add a KycDataRequest to an array that contains it', () => {
        const kycDataRequest: IKycDataRequest = sampleWithRequiredData;
        const kycDataRequestCollection: IKycDataRequest[] = [
          {
            ...kycDataRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycDataRequestToCollectionIfMissing(kycDataRequestCollection, kycDataRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycDataRequest to an array that doesn't contain it", () => {
        const kycDataRequest: IKycDataRequest = sampleWithRequiredData;
        const kycDataRequestCollection: IKycDataRequest[] = [sampleWithPartialData];
        expectedResult = service.addKycDataRequestToCollectionIfMissing(kycDataRequestCollection, kycDataRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycDataRequest);
      });

      it('should add only unique KycDataRequest to an array', () => {
        const kycDataRequestArray: IKycDataRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycDataRequestCollection: IKycDataRequest[] = [sampleWithRequiredData];
        expectedResult = service.addKycDataRequestToCollectionIfMissing(kycDataRequestCollection, ...kycDataRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycDataRequest: IKycDataRequest = sampleWithRequiredData;
        const kycDataRequest2: IKycDataRequest = sampleWithPartialData;
        expectedResult = service.addKycDataRequestToCollectionIfMissing([], kycDataRequest, kycDataRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycDataRequest);
        expect(expectedResult).toContain(kycDataRequest2);
      });

      it('should accept null and undefined values', () => {
        const kycDataRequest: IKycDataRequest = sampleWithRequiredData;
        expectedResult = service.addKycDataRequestToCollectionIfMissing([], null, kycDataRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kycDataRequest);
      });

      it('should return initial array if no KycDataRequest is added', () => {
        const kycDataRequestCollection: IKycDataRequest[] = [sampleWithRequiredData];
        expectedResult = service.addKycDataRequestToCollectionIfMissing(kycDataRequestCollection, undefined, null);
        expect(expectedResult).toEqual(kycDataRequestCollection);
      });
    });

    describe('compareKycDataRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycDataRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 32543 };
        const entity2 = null;

        const compareResult1 = service.compareKycDataRequest(entity1, entity2);
        const compareResult2 = service.compareKycDataRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 32543 };
        const entity2 = { id: 4869 };

        const compareResult1 = service.compareKycDataRequest(entity1, entity2);
        const compareResult2 = service.compareKycDataRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 32543 };
        const entity2 = { id: 32543 };

        const compareResult1 = service.compareKycDataRequest(entity1, entity2);
        const compareResult2 = service.compareKycDataRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
