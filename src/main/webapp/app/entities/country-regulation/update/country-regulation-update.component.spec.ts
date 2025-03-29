import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CountryRegulationService } from '../service/country-regulation.service';
import { ICountryRegulation } from '../country-regulation.model';
import { CountryRegulationFormService } from './country-regulation-form.service';

import { CountryRegulationUpdateComponent } from './country-regulation-update.component';

describe('CountryRegulation Management Update Component', () => {
  let comp: CountryRegulationUpdateComponent;
  let fixture: ComponentFixture<CountryRegulationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countryRegulationFormService: CountryRegulationFormService;
  let countryRegulationService: CountryRegulationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CountryRegulationUpdateComponent],
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
      .overrideTemplate(CountryRegulationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountryRegulationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countryRegulationFormService = TestBed.inject(CountryRegulationFormService);
    countryRegulationService = TestBed.inject(CountryRegulationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const countryRegulation: ICountryRegulation = { id: 14128 };

      activatedRoute.data = of({ countryRegulation });
      comp.ngOnInit();

      expect(comp.countryRegulation).toEqual(countryRegulation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICountryRegulation>>();
      const countryRegulation = { id: 18454 };
      jest.spyOn(countryRegulationFormService, 'getCountryRegulation').mockReturnValue(countryRegulation);
      jest.spyOn(countryRegulationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countryRegulation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countryRegulation }));
      saveSubject.complete();

      // THEN
      expect(countryRegulationFormService.getCountryRegulation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(countryRegulationService.update).toHaveBeenCalledWith(expect.objectContaining(countryRegulation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICountryRegulation>>();
      const countryRegulation = { id: 18454 };
      jest.spyOn(countryRegulationFormService, 'getCountryRegulation').mockReturnValue({ id: null });
      jest.spyOn(countryRegulationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countryRegulation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countryRegulation }));
      saveSubject.complete();

      // THEN
      expect(countryRegulationFormService.getCountryRegulation).toHaveBeenCalled();
      expect(countryRegulationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICountryRegulation>>();
      const countryRegulation = { id: 18454 };
      jest.spyOn(countryRegulationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countryRegulation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countryRegulationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
