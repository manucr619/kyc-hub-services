import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BankService } from '../service/bank.service';
import { IBank } from '../bank.model';
import { BankFormService } from './bank-form.service';

import { BankUpdateComponent } from './bank-update.component';

describe('Bank Management Update Component', () => {
  let comp: BankUpdateComponent;
  let fixture: ComponentFixture<BankUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankFormService: BankFormService;
  let bankService: BankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BankUpdateComponent],
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
      .overrideTemplate(BankUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankFormService = TestBed.inject(BankFormService);
    bankService = TestBed.inject(BankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bank: IBank = { id: 6074 };

      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      expect(comp.bank).toEqual(bank);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBank>>();
      const bank = { id: 16728 };
      jest.spyOn(bankFormService, 'getBank').mockReturnValue(bank);
      jest.spyOn(bankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bank }));
      saveSubject.complete();

      // THEN
      expect(bankFormService.getBank).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankService.update).toHaveBeenCalledWith(expect.objectContaining(bank));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBank>>();
      const bank = { id: 16728 };
      jest.spyOn(bankFormService, 'getBank').mockReturnValue({ id: null });
      jest.spyOn(bankService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bank }));
      saveSubject.complete();

      // THEN
      expect(bankFormService.getBank).toHaveBeenCalled();
      expect(bankService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBank>>();
      const bank = { id: 16728 };
      jest.spyOn(bankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
