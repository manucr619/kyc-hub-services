import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { APIAuditLogService } from '../service/api-audit-log.service';
import { IAPIAuditLog } from '../api-audit-log.model';
import { APIAuditLogFormService } from './api-audit-log-form.service';

import { APIAuditLogUpdateComponent } from './api-audit-log-update.component';

describe('APIAuditLog Management Update Component', () => {
  let comp: APIAuditLogUpdateComponent;
  let fixture: ComponentFixture<APIAuditLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPIAuditLogFormService: APIAuditLogFormService;
  let aPIAuditLogService: APIAuditLogService;
  let bankService: BankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [APIAuditLogUpdateComponent],
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
      .overrideTemplate(APIAuditLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APIAuditLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPIAuditLogFormService = TestBed.inject(APIAuditLogFormService);
    aPIAuditLogService = TestBed.inject(APIAuditLogService);
    bankService = TestBed.inject(BankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bank query and add missing value', () => {
      const aPIAuditLog: IAPIAuditLog = { id: 18935 };
      const bank: IBank = { id: 16728 };
      aPIAuditLog.bank = bank;

      const bankCollection: IBank[] = [{ id: 16728 }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [bank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aPIAuditLog });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining),
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aPIAuditLog: IAPIAuditLog = { id: 18935 };
      const bank: IBank = { id: 16728 };
      aPIAuditLog.bank = bank;

      activatedRoute.data = of({ aPIAuditLog });
      comp.ngOnInit();

      expect(comp.banksSharedCollection).toContainEqual(bank);
      expect(comp.aPIAuditLog).toEqual(aPIAuditLog);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAPIAuditLog>>();
      const aPIAuditLog = { id: 23832 };
      jest.spyOn(aPIAuditLogFormService, 'getAPIAuditLog').mockReturnValue(aPIAuditLog);
      jest.spyOn(aPIAuditLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPIAuditLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPIAuditLog }));
      saveSubject.complete();

      // THEN
      expect(aPIAuditLogFormService.getAPIAuditLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPIAuditLogService.update).toHaveBeenCalledWith(expect.objectContaining(aPIAuditLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAPIAuditLog>>();
      const aPIAuditLog = { id: 23832 };
      jest.spyOn(aPIAuditLogFormService, 'getAPIAuditLog').mockReturnValue({ id: null });
      jest.spyOn(aPIAuditLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPIAuditLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPIAuditLog }));
      saveSubject.complete();

      // THEN
      expect(aPIAuditLogFormService.getAPIAuditLog).toHaveBeenCalled();
      expect(aPIAuditLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAPIAuditLog>>();
      const aPIAuditLog = { id: 23832 };
      jest.spyOn(aPIAuditLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPIAuditLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPIAuditLogService.update).toHaveBeenCalled();
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
  });
});
