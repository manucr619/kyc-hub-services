import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICountryRegulation } from 'app/entities/country-regulation/country-regulation.model';
import { CountryRegulationService } from 'app/entities/country-regulation/service/country-regulation.service';
import { CustomerService } from '../service/customer.service';
import { ICustomer } from '../customer.model';
import { CustomerFormService } from './customer-form.service';

import { CustomerUpdateComponent } from './customer-update.component';

describe('Customer Management Update Component', () => {
  let comp: CustomerUpdateComponent;
  let fixture: ComponentFixture<CustomerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerFormService: CustomerFormService;
  let customerService: CustomerService;
  let countryRegulationService: CountryRegulationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CustomerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerFormService = TestBed.inject(CustomerFormService);
    customerService = TestBed.inject(CustomerService);
    countryRegulationService = TestBed.inject(CountryRegulationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CountryRegulation query and add missing value', () => {
      const customer: ICustomer = { id: 21032 };
      const countryRegulation: ICountryRegulation = { id: 18454 };
      customer.countryRegulation = countryRegulation;

      const countryRegulationCollection: ICountryRegulation[] = [{ id: 18454 }];
      jest.spyOn(countryRegulationService, 'query').mockReturnValue(of(new HttpResponse({ body: countryRegulationCollection })));
      const additionalCountryRegulations = [countryRegulation];
      const expectedCollection: ICountryRegulation[] = [...additionalCountryRegulations, ...countryRegulationCollection];
      jest.spyOn(countryRegulationService, 'addCountryRegulationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(countryRegulationService.query).toHaveBeenCalled();
      expect(countryRegulationService.addCountryRegulationToCollectionIfMissing).toHaveBeenCalledWith(
        countryRegulationCollection,
        ...additionalCountryRegulations.map(expect.objectContaining),
      );
      expect(comp.countryRegulationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customer: ICustomer = { id: 21032 };
      const countryRegulation: ICountryRegulation = { id: 18454 };
      customer.countryRegulation = countryRegulation;

      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      expect(comp.countryRegulationsSharedCollection).toContainEqual(countryRegulation);
      expect(comp.customer).toEqual(customer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 26915 };
      jest.spyOn(customerFormService, 'getCustomer').mockReturnValue(customer);
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(customerFormService.getCustomer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerService.update).toHaveBeenCalledWith(expect.objectContaining(customer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 26915 };
      jest.spyOn(customerFormService, 'getCustomer').mockReturnValue({ id: null });
      jest.spyOn(customerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customer }));
      saveSubject.complete();

      // THEN
      expect(customerFormService.getCustomer).toHaveBeenCalled();
      expect(customerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomer>>();
      const customer = { id: 26915 };
      jest.spyOn(customerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCountryRegulation', () => {
      it('Should forward to countryRegulationService', () => {
        const entity = { id: 18454 };
        const entity2 = { id: 14128 };
        jest.spyOn(countryRegulationService, 'compareCountryRegulation');
        comp.compareCountryRegulation(entity, entity2);
        expect(countryRegulationService.compareCountryRegulation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
