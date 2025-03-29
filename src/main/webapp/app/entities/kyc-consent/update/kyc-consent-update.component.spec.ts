import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IKycConsent } from '../kyc-consent.model';
import { KycConsentService } from '../service/kyc-consent.service';
import { KycConsentFormService } from './kyc-consent-form.service';

import { KycConsentUpdateComponent } from './kyc-consent-update.component';

describe('KycConsent Management Update Component', () => {
  let comp: KycConsentUpdateComponent;
  let fixture: ComponentFixture<KycConsentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kycConsentFormService: KycConsentFormService;
  let kycConsentService: KycConsentService;
  let customerService: CustomerService;
  let bankService: BankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [KycConsentUpdateComponent],
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
      .overrideTemplate(KycConsentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KycConsentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycConsentFormService = TestBed.inject(KycConsentFormService);
    kycConsentService = TestBed.inject(KycConsentService);
    customerService = TestBed.inject(CustomerService);
    bankService = TestBed.inject(BankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const kycConsent: IKycConsent = { id: 2003 };
      const customer: ICustomer = { id: 26915 };
      kycConsent.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bank query and add missing value', () => {
      const kycConsent: IKycConsent = { id: 2003 };
      const issuerBank: IBank = { id: 16728 };
      kycConsent.issuerBank = issuerBank;
      const recipientBank: IBank = { id: 16728 };
      kycConsent.recipientBank = recipientBank;

      const bankCollection: IBank[] = [{ id: 16728 }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [issuerBank, recipientBank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining),
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const kycConsent: IKycConsent = { id: 2003 };
      const customer: ICustomer = { id: 26915 };
      kycConsent.customer = customer;
      const issuerBank: IBank = { id: 16728 };
      kycConsent.issuerBank = issuerBank;
      const recipientBank: IBank = { id: 16728 };
      kycConsent.recipientBank = recipientBank;

      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.banksSharedCollection).toContainEqual(issuerBank);
      expect(comp.banksSharedCollection).toContainEqual(recipientBank);
      expect(comp.kycConsent).toEqual(kycConsent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentFormService, 'getKycConsent').mockReturnValue(kycConsent);
      jest.spyOn(kycConsentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycConsent }));
      saveSubject.complete();

      // THEN
      expect(kycConsentFormService.getKycConsent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycConsentService.update).toHaveBeenCalledWith(expect.objectContaining(kycConsent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentFormService, 'getKycConsent').mockReturnValue({ id: null });
      jest.spyOn(kycConsentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycConsent }));
      saveSubject.complete();

      // THEN
      expect(kycConsentFormService.getKycConsent).toHaveBeenCalled();
      expect(kycConsentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycConsent>>();
      const kycConsent = { id: 12952 };
      jest.spyOn(kycConsentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycConsent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycConsentService.update).toHaveBeenCalled();
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
  });
});
