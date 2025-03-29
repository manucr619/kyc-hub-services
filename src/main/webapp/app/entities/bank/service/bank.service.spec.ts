import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBank } from '../bank.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bank.test-samples';

import { BankService } from './bank.service';

const requireRestSample: IBank = {
  ...sampleWithRequiredData,
};

describe('Bank Service', () => {
  let service: BankService;
  let httpMock: HttpTestingController;
  let expectedResult: IBank | IBank[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BankService);
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

    it('should create a Bank', () => {
      const bank = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bank).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bank', () => {
      const bank = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bank).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bank', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bank', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bank', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBankToCollectionIfMissing', () => {
      it('should add a Bank to an empty array', () => {
        const bank: IBank = sampleWithRequiredData;
        expectedResult = service.addBankToCollectionIfMissing([], bank);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bank);
      });

      it('should not add a Bank to an array that contains it', () => {
        const bank: IBank = sampleWithRequiredData;
        const bankCollection: IBank[] = [
          {
            ...bank,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, bank);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bank to an array that doesn't contain it", () => {
        const bank: IBank = sampleWithRequiredData;
        const bankCollection: IBank[] = [sampleWithPartialData];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, bank);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bank);
      });

      it('should add only unique Bank to an array', () => {
        const bankArray: IBank[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bankCollection: IBank[] = [sampleWithRequiredData];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, ...bankArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bank: IBank = sampleWithRequiredData;
        const bank2: IBank = sampleWithPartialData;
        expectedResult = service.addBankToCollectionIfMissing([], bank, bank2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bank);
        expect(expectedResult).toContain(bank2);
      });

      it('should accept null and undefined values', () => {
        const bank: IBank = sampleWithRequiredData;
        expectedResult = service.addBankToCollectionIfMissing([], null, bank, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bank);
      });

      it('should return initial array if no Bank is added', () => {
        const bankCollection: IBank[] = [sampleWithRequiredData];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, undefined, null);
        expect(expectedResult).toEqual(bankCollection);
      });
    });

    describe('compareBank', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBank(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 16728 };
        const entity2 = null;

        const compareResult1 = service.compareBank(entity1, entity2);
        const compareResult2 = service.compareBank(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 16728 };
        const entity2 = { id: 6074 };

        const compareResult1 = service.compareBank(entity1, entity2);
        const compareResult2 = service.compareBank(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 16728 };
        const entity2 = { id: 16728 };

        const compareResult1 = service.compareBank(entity1, entity2);
        const compareResult2 = service.compareBank(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
