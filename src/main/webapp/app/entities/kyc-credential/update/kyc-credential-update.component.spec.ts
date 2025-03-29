import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IKycCredential } from '../kyc-credential.model';
import { KycCredentialService } from '../service/kyc-credential.service';
import { KycCredentialFormService } from './kyc-credential-form.service';

import { KycCredentialUpdateComponent } from './kyc-credential-update.component';

describe('KycCredential Management Update Component', () => {
  let comp: KycCredentialUpdateComponent;
  let fixture: ComponentFixture<KycCredentialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let kycCredentialFormService: KycCredentialFormService;
  let kycCredentialService: KycCredentialService;
  let bankService: BankService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [KycCredentialUpdateComponent],
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
      .overrideTemplate(KycCredentialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KycCredentialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    kycCredentialFormService = TestBed.inject(KycCredentialFormService);
    kycCredentialService = TestBed.inject(KycCredentialService);
    bankService = TestBed.inject(BankService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bank query and add missing value', () => {
      const kycCredential: IKycCredential = { id: 27482 };
      const issuer: IBank = { id: 16728 };
      kycCredential.issuer = issuer;

      const bankCollection: IBank[] = [{ id: 16728 }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [issuer];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycCredential });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining),
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const kycCredential: IKycCredential = { id: 27482 };
      const customer: ICustomer = { id: 26915 };
      kycCredential.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 26915 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ kycCredential });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const kycCredential: IKycCredential = { id: 27482 };
      const issuer: IBank = { id: 16728 };
      kycCredential.issuer = issuer;
      const customer: ICustomer = { id: 26915 };
      kycCredential.customer = customer;

      activatedRoute.data = of({ kycCredential });
      comp.ngOnInit();

      expect(comp.banksSharedCollection).toContainEqual(issuer);
      expect(comp.customersSharedCollection).toContainEqual(customer);
      expect(comp.kycCredential).toEqual(kycCredential);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCredential>>();
      const kycCredential = { id: 24791 };
      jest.spyOn(kycCredentialFormService, 'getKycCredential').mockReturnValue(kycCredential);
      jest.spyOn(kycCredentialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCredential });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycCredential }));
      saveSubject.complete();

      // THEN
      expect(kycCredentialFormService.getKycCredential).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(kycCredentialService.update).toHaveBeenCalledWith(expect.objectContaining(kycCredential));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCredential>>();
      const kycCredential = { id: 24791 };
      jest.spyOn(kycCredentialFormService, 'getKycCredential').mockReturnValue({ id: null });
      jest.spyOn(kycCredentialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCredential: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: kycCredential }));
      saveSubject.complete();

      // THEN
      expect(kycCredentialFormService.getKycCredential).toHaveBeenCalled();
      expect(kycCredentialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IKycCredential>>();
      const kycCredential = { id: 24791 };
      jest.spyOn(kycCredentialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ kycCredential });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(kycCredentialService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBank', () => {
      it('Should forward to bankService', () => {
        const entity = { id: 16728 };
        const entity2 = { id: 6074 };
        jest.spyOn(bankService, 'compareBank');
        comp.compareBank(entity, entity2);
        expect(bankService.compareBank).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 26915 };
        const entity2 = { id: 21032 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
