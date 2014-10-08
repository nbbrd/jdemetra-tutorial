/*
 * Copyright 2013-2014 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package be.nbb.demetra.tutorial.plugin.sadiags;

import ec.satoolkit.diagnostics.FTest;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.IDiagnostics;
import ec.tstoolkit.algorithm.ProcQuality;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.modelling.arima.tramo.SeasonalityTests;
import ec.tstoolkit.stats.StatisticalTest;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jean Palate
 */
public class MyDiagnostics implements IDiagnostics {

    static final String QS_SA = "Qs test on SA", QS_I = "Qs test on I", FTEST_SA = "F-Test on SA (seasonal dummies)", FTEST_I = "F-Test on I (seasonal dummies)";
    static final String[] ALL = new String[]{QS_SA, QS_I, FTEST_SA, FTEST_I};
    private StatisticalTest qs_sa, qs_i, f_sa, f_i;

    static IDiagnostics create(CompositeResults rslts, MyDiagnosticsConfiguration config) {
        try {
            MyDiagnostics test = new MyDiagnostics();
            TsData sa = rslts.getData(ModellingDictionary.SA_LIN, TsData.class);
            TsData i = rslts.getData(ModellingDictionary.I_LIN, TsData.class);
            if (sa == null && i == null) {
                return null;
            }
            if (sa != null) {
                SeasonalityTests satest = SeasonalityTests.seasonalityTest(sa, 1, true, true);
                test.qs_sa = satest.getQs();
                FTest F = new FTest();
                if (F.test(sa)) {
                    test.f_sa = F.getFTest();
                }
            }
            if (i != null) {
                SeasonalityTests itest = SeasonalityTests.seasonalityTest(i, 0, true, true);
                test.qs_i = itest.getQs();
                FTest F = new FTest();
                if (F.test(i)) {
                    test.f_i = F.getFTest();
                }
            }
            return test;
        } catch (Exception err) {
            return null;
        }
    }

    @Override
    public String getName() {
        return MyDiagnosticsFactory.NAME;
    }

    @Override
    public List<String> getTests() {
        ArrayList<String> tests = new ArrayList<String>();
        if (qs_sa != null) {
            tests.add(QS_SA);
        }
        if (f_sa != null) {
            tests.add(FTEST_SA);
        }
        if (qs_i != null) {
            tests.add(QS_I);
        }
        if (f_i != null) {
            tests.add(FTEST_I);
        }
        return tests;
    }

    @Override
    public ProcQuality getDiagnostic(String test) {
        switch (test) {
            case QS_SA:
                return quality(qs_sa);

            case FTEST_SA:
                return quality(f_sa);
            case QS_I:
                return quality(qs_i);

            case FTEST_I:
                return quality(f_i);

            default:
                return ProcQuality.Undefined;
        }
    }

    @Override
    public double getValue(String test) {

        switch (test) {
            case QS_SA:
                return pvalue(qs_sa);

            case FTEST_SA:
                return pvalue(f_sa);
            case QS_I:
                return pvalue(qs_i);

            case FTEST_I:
                return pvalue(f_i);

            default:
                return Double.NaN;
        }
    }

    @Override
    public List<String> getWarnings() {
        return Collections.EMPTY_LIST;
    }

    private ProcQuality quality(StatisticalTest test) {
        if (test == null) {
            return ProcQuality.Undefined;
        }
        double pval = test.getPValue();
        if (pval < .001) {
            return ProcQuality.Severe;
        } else if (pval < .01) {
            return ProcQuality.Bad;
        } else if (pval < .05) {
            return ProcQuality.Uncertain;
        } else {
            return ProcQuality.Good;
        }
    }

    private double pvalue(StatisticalTest test) {
        return test == null ? Double.NaN : test.getPValue();
    }
}
