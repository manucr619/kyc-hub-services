import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IKycCredential } from '../kyc-credential.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../kyc-credential.test-samples';

import { KycCredentialService, RestKycCredential } from './kyc-credential.service';

const requireRestSample: RestKycCredential = {
  ...sampleWithRequiredData,
  issuedDate: sampleWithRequiredData.issuedDate?.toJSON(),
  expiryDate: sampleWithRequiredData.expiryDate?.toJSON(),
};

describe('KycCredential Service', () => {
  let service: KycCredentialService;
  let httpMock: HttpTestingController;
  let expectedResult: IKycCredential | IKycCredential[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(KycCredentialService);
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

    it('should create a KycCredential', () => {
      const kycCredential = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(kycCredential).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a KycCredential', () => {
      const kycCredential = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(kycCredential).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a KycCredential', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of KycCredential', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a KycCredential', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addKycCredentialToCollectionIfMissing', () => {
      it('should add a KycCredential to an empty array', () => {
        const kycCredential: IKycCredential = sampleWithRequiredData;
        expectedResult = service.addKycCredentialToCollectionIfMissing([], kycCredential);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kycCredential);
      });

      it('should not add a KycCredential to an array that contains it', () => {
        const kycCredential: IKycCredential = sampleWithRequiredData;
        const kycCredentialCollection: IKycCredential[] = [
          {
            ...kycCredential,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addKycCredentialToCollectionIfMissing(kycCredentialCollection, kycCredential);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a KycCredential to an array that doesn't contain it", () => {
        const kycCredential: IKycCredential = sampleWithRequiredData;
        const kycCredentialCollection: IKycCredential[] = [sampleWithPartialData];
        expectedResult = service.addKycCredentialToCollectionIfMissing(kycCredentialCollection, kycCredential);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycCredential);
      });

      it('should add only unique KycCredential to an array', () => {
        const kycCredentialArray: IKycCredential[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const kycCredentialCollection: IKycCredential[] = [sampleWithRequiredData];
        expectedResult = service.addKycCredentialToCollectionIfMissing(kycCredentialCollection, ...kycCredentialArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const kycCredential: IKycCredential = sampleWithRequiredData;
        const kycCredential2: IKycCredential = sampleWithPartialData;
        expectedResult = service.addKycCredentialToCollectionIfMissing([], kycCredential, kycCredential2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(kycCredential);
        expect(expectedResult).toContain(kycCredential2);
      });

      it('should accept null and undefined values', () => {
        const kycCredential: IKycCredential = sampleWithRequiredData;
        expectedResult = service.addKycCredentialToCollectionIfMissing([], null, kycCredential, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(kycCredential);
      });

      it('should return initial array if no KycCredential is added', () => {
        const kycCredentialCollection: IKycCredential[] = [sampleWithRequiredData];
        expectedResult = service.addKycCredentialToCollectionIfMissing(kycCredentialCollection, undefined, null);
        expect(expectedResult).toEqual(kycCredentialCollection);
      });
    });

    describe('compareKycCredential', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareKycCredential(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 24791 };
        const entity2 = null;

        const compareResult1 = service.compareKycCredential(entity1, entity2);
        const compareResult2 = service.compareKycCredential(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 24791 };
        const entity2 = { id: 27482 };

        const compareResult1 = service.compareKycCredential(entity1, entity2);
        const compareResult2 = service.compareKycCredential(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 24791 };
        const entity2 = { id: 24791 };

        const compareResult1 = service.compareKycCredential(entity1, entity2);
        const compareResult2 = service.compareKycCredential(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
