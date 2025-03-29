import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IKycConsent } from 'app/entities/kyc-consent/kyc-consent.model';
import { KycConsentService } from 'app/entities/kyc-consent/service/kyc-consent.service';
import { IKycDataRequest } from '../kyc-data-request.model';
import { KycDataRequestService } from '../service/kyc-data-request.service';
import { KycDataRequestFormService } from './kyc-data-request-form.service';

import { KycDataRequestUpdateComponent } from './kyc-data-request-update.component';

describe('KycDataRequest Management Update Component', () => {
  let comp: KycDataRequestUpdateComponent;
  let fixture: ComponentFixture<KycDataRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kycDataRequestFormService: KycDataRequestFormService;
  let kycDataRequestService: KycDataRequestService;
  let customerService: CustomerService;
  let bankService: BankService;
  let kycConsentService: KycConsentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [KycDataRequestUpdateComponent],
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
      .overrideTemplate(KycDataRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KycDataRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycDataRequestFormService = TestBed.inject(KycDataRequestFormService);
    kycDataRequestService = TestBed.inject(KycDataRequestService);
    customerService = TestBed.inject(CustomerService);
    bankService = TestBed.inject(BankService);
    kycConsentService = TestBed.inject(KycConsentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const kycDataRequest: IKycDataRequest = { id: 4869 };
      const customer: ICustomer = { id: 26915 };
      kycDataRequest.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bank query and add missing value', () => {
      const kycDataRequest: IKycDataRequest = { id: 4869 };
      const fromBank: IBank = { id: 16728 };
      kycDataRequest.fromBank = fromBank;
      const toBank: IBank = { id: 16728 };
      kycDataRequest.toBank = toBank;

      const bankCollection: IBank[] = [{ id: 16728 }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [fromBank, toBank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining),
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call KycConsent query and add missing value', () => {
      const kycDataRequest: IKycDataRequest = { id: 4869 };
      const consent: IKycConsent = { id: 12952 };
      kycDataRequest.consent = consent;

      const kycConsentCollection: IKycConsent[] = [{ id: 12952 }];
      jest.spyOn(kycConsentService, 'query').mockReturnValue(of(new HttpResponse({ body: kycConsentCollection })));
      const additionalKycConsents = [consent];
      const expectedCollection: IKycConsent[] = [...additionalKycConsents, ...kycConsentCollection];
      jest.spyOn(kycConsentService, 'addKycConsentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      expect(kycConsentService.query).toHaveBeenCalled();
      expect(kycConsentService.addKycConsentToCollectionIfMissing).toHaveBeenCalledWith(
        kycConsentCollection,
        ...additionalKycConsents.map(expect.objectContaining),
      );
      expect(comp.kycConsentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const kycDataRequest: IKycDataRequest = { id: 4869 };
      const customer: ICustomer = { id: 26915 };
      kycDataRequest.customer = customer;
      const fromBank: IBank = { id: 16728 };
      kycDataRequest.fromBank = fromBank;
      const toBank: IBank = { id: 16728 };
      kycDataRequest.toBank = toBank;
      const consent: IKycConsent = { id: 12952 };
      kycDataRequest.consent = consent;

      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.banksSharedCollection).toContainEqual(fromBank);
      expect(comp.banksSharedCollection).toContainEqual(toBank);
      expect(comp.kycConsentsSharedCollection).toContainEqual(consent);
      expect(comp.kycDataRequest).toEqual(kycDataRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDataRequest>>();
      const kycDataRequest = { id: 32543 };
      jest.spyOn(kycDataRequestFormService, 'getKycDataRequest').mockReturnValue(kycDataRequest);
      jest.spyOn(kycDataRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycDataRequest }));
      saveSubject.complete();

      // THEN
      expect(kycDataRequestFormService.getKycDataRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycDataRequestService.update).toHaveBeenCalledWith(expect.objectContaining(kycDataRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDataRequest>>();
      const kycDataRequest = { id: 32543 };
      jest.spyOn(kycDataRequestFormService, 'getKycDataRequest').mockReturnValue({ id: null });
      jest.spyOn(kycDataRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDataRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycDataRequest }));
      saveSubject.complete();

      // THEN
      expect(kycDataRequestFormService.getKycDataRequest).toHaveBeenCalled();
      expect(kycDataRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycDataRequest>>();
      const kycDataRequest = { id: 32543 };
      jest.spyOn(kycDataRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycDataRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycDataRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 26915 };
        const entity2 = { id: 21032 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBank', () => {
      it('Should forward to bankService', () => {
        const entity = { id: 16728 };
        const entity2 = { id: 6074 };
        jest.spyOn(bankService, 'compareBank');
        comp.compareBank(entity, entity2);
        expect(bankService.compareBank).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareKycConsent', () => {
      it('Should forward to kycConsentService', () => {
        const entity = { id: 12952 };
        const entity2 = { id: 2003 };
        jest.spyOn(kycConsentService, 'compareKycConsent');
        comp.compareKycConsent(entity, entity2);
        expect(kycConsentService.compareKycConsent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
