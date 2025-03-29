import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CountryRegulationDetailComponent } from './country-regulation-detail.component';

describe('CountryRegulation Management Detail Component', () => {
  let comp: CountryRegulationDetailComponent;
  let fixture: ComponentFixture<CountryRegulationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountryRegulationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./country-regulation-detail.component').then(m => m.CountryRegulationDetailComponent),
              resolve: { countryRegulation: () => of({ id: 18454 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CountryRegulationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CountryRegulationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countryRegulation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CountryRegulationDetailComponent);

      // THEN
      expect(instance.countryRegulation()).toEqual(expect.objectContaining({ id: 18454 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
