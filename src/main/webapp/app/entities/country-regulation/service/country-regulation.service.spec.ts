import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICountryRegulation } from '../country-regulation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../country-regulation.test-samples';

import { CountryRegulationService } from './country-regulation.service';

const requireRestSample: ICountryRegulation = {
  ...sampleWithRequiredData,
};

describe('CountryRegulation Service', () => {
  let service: CountryRegulationService;
  let httpMock: HttpTestingController;
  let expectedResult: ICountryRegulation | ICountryRegulation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CountryRegulationService);
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

    it('should create a CountryRegulation', () => {
      const countryRegulation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(countryRegulation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountryRegulation', () => {
      const countryRegulation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(countryRegulation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountryRegulation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountryRegulation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CountryRegulation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCountryRegulationToCollectionIfMissing', () => {
      it('should add a CountryRegulation to an empty array', () => {
        const countryRegulation: ICountryRegulation = sampleWithRequiredData;
        expectedResult = service.addCountryRegulationToCollectionIfMissing([], countryRegulation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countryRegulation);
      });

      it('should not add a CountryRegulation to an array that contains it', () => {
        const countryRegulation: ICountryRegulation = sampleWithRequiredData;
        const countryRegulationCollection: ICountryRegulation[] = [
          {
            ...countryRegulation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCountryRegulationToCollectionIfMissing(countryRegulationCollection, countryRegulation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountryRegulation to an array that doesn't contain it", () => {
        const countryRegulation: ICountryRegulation = sampleWithRequiredData;
        const countryRegulationCollection: ICountryRegulation[] = [sampleWithPartialData];
        expectedResult = service.addCountryRegulationToCollectionIfMissing(countryRegulationCollection, countryRegulation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countryRegulation);
      });

      it('should add only unique CountryRegulation to an array', () => {
        const countryRegulationArray: ICountryRegulation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const countryRegulationCollection: ICountryRegulation[] = [sampleWithRequiredData];
        expectedResult = service.addCountryRegulationToCollectionIfMissing(countryRegulationCollection, ...countryRegulationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countryRegulation: ICountryRegulation = sampleWithRequiredData;
        const countryRegulation2: ICountryRegulation = sampleWithPartialData;
        expectedResult = service.addCountryRegulationToCollectionIfMissing([], countryRegulation, countryRegulation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countryRegulation);
        expect(expectedResult).toContain(countryRegulation2);
      });

      it('should accept null and undefined values', () => {
        const countryRegulation: ICountryRegulation = sampleWithRequiredData;
        expectedResult = service.addCountryRegulationToCollectionIfMissing([], null, countryRegulation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countryRegulation);
      });

      it('should return initial array if no CountryRegulation is added', () => {
        const countryRegulationCollection: ICountryRegulation[] = [sampleWithRequiredData];
        expectedResult = service.addCountryRegulationToCollectionIfMissing(countryRegulationCollection, undefined, null);
        expect(expectedResult).toEqual(countryRegulationCollection);
      });
    });

    describe('compareCountryRegulation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCountryRegulation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 18454 };
        const entity2 = null;

        const compareResult1 = service.compareCountryRegulation(entity1, entity2);
        const compareResult2 = service.compareCountryRegulation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 18454 };
        const entity2 = { id: 14128 };

        const compareResult1 = service.compareCountryRegulation(entity1, entity2);
        const compareResult2 = service.compareCountryRegulation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 18454 };
        const entity2 = { id: 18454 };

        const compareResult1 = service.compareCountryRegulation(entity1, entity2);
        const compareResult2 = service.compareCountryRegulation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
