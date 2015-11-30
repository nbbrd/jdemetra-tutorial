/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seasonality;

import ec.satoolkit.ISeriesDecomposition;
import ec.satoolkit.diagnostics.FTest;
import ec.satoolkit.diagnostics.KruskalWallisTest;
import ec.tss.sa.documents.SaDocument;
import ec.tstoolkit.algorithm.IProcResults;
import ec.tstoolkit.algorithm.ProcessingInformation;
import ec.tstoolkit.information.StatisticalTest;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.modelling.arima.PreprocessingModel;
import ec.tstoolkit.modelling.arima.tramo.SeasonalityTests;
import ec.tstoolkit.timeseries.TsPeriodSelector;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jean Palate
 */
public class SeasonalityTestsResults implements IProcResults {

    private final Map<String, StatisticalTest> tests = new HashMap<>();

    public static class Information {

        public TsData s;
        public int del;
        public boolean mean = true;
        public boolean mul;
    }

    public SeasonalityTestsResults(SaDocument doc, List<String> series, List<String> tests, int nlast) {
        // create the tests
        try {
            List<String> ls = new ArrayList<>();
            List<String> alls = new ArrayList<>();
            for (String se : series) {
                int ilast = se.indexOf("_last");
                if (ilast < 0) {
                    alls.add(se);
                } else {
                    ls.add(se.substring(0, ilast));
                }
            }
            for (String se : alls) {
                Information information = buildInfo(se, doc, 0);
                if (information != null) {
                    TsData s = information.s;

                    if (information.mul) {
                        s = s.log();
                    }
                    SeasonalityTests seasonalityTest = SeasonalityTests.seasonalityTest(s, information.del, information.mean, true);
                    for (String t : tests) {
                        addTest(seasonalityTest, se, t, true);
                    }
                }
            }
            for (String se : ls) {
                Information information = buildInfo(se, doc, nlast);
                if (information != null) {
                    TsData s = information.s;
                    if (information.mul) {
                        s = s.log();
                    }
                    SeasonalityTests seasonalityTest = SeasonalityTests.seasonalityTest(s, information.del, information.mean, true);
                    for (String t : tests) {
                        addTest(seasonalityTest, se, t, false);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public <T> T
            getData(String id, Class<T> tclass
            ) {
        if (!tclass.isAssignableFrom(StatisticalTest.class)) {
            return null;
        }
        return (T) tests.get(id);
    }

    @Override
    public boolean contains(String id
    ) {
        return tests.containsKey(id);
    }

    @Override
    public Map<String, Class> getDictionary() {
        HashMap<String, Class> dic = new HashMap<>();
        for (String key : tests.keySet()) {
            dic.put(key, StatisticalTest.class);
        }
        return dic;
    }

    @Override
    public List<ProcessingInformation> getProcessingInformation() {
        return Collections.EMPTY_LIST;
    }

    private void addTest(SeasonalityTests test, String s, String t, boolean all) {
        StatisticalTest st = null;
        switch (t) {
            case (CsvSeasonalityOutputConfiguration.QS):
                st = StatisticalTest.create(test.getQs());
                break;
            case (CsvSeasonalityOutputConfiguration.F):
                st = StatisticalTest.create(test.getNonParametricTest());
                break;
            case (CsvSeasonalityOutputConfiguration.KW):
                st = StatisticalTest.create(new KruskalWallisTest(test.getDifferencing().differenced));
                break;
            case (CsvSeasonalityOutputConfiguration.P):
                st = StatisticalTest.create(test.getPeriodogramTest());
                break;
            case (CsvSeasonalityOutputConfiguration.SD):
                FTest ftest = new FTest();
                ftest.test(test.getDifferencing().original);
                st = StatisticalTest.create(ftest.getFTest());
                break;
        }
        if (st != null) {
            tests.put(CsvSeasonalityOutputConfiguration.item(all ? s : s + CsvSeasonalityOutputConfiguration.LAST, t), st);
        }
    }

    private Information buildInfo(String s, SaDocument source, int last) {

        Information info = new Information();
        info.mean = true;
        switch (s) {
            case ModellingDictionary.FULL_RES:
                info.del = 0;
                info.mean = false;
                break;
            case ModellingDictionary.I_CMP:
                info.del = 0;
                info.mean = true;
                break;
            default:
                info.del = 1;
                info.mean = true;
                break;
        }
        PreprocessingModel preprocessingPart = source.getPreprocessingPart();
        ISeriesDecomposition finals = source.getFinalDecomposition();
        if (preprocessingPart != null) {
            switch (s) {
                case ModellingDictionary.Y:
                    info.s = preprocessingPart.description.transformedOriginal();
                    info.mul = false;
                    break;
                case ModellingDictionary.FULL_RES:
                    info.s = preprocessingPart.getFullResiduals();
                    info.mul = false;
                    break;
                default:
                    info.s = source.getResults().getData(s, TsData.class);
                    info.mul = preprocessingPart.isMultiplicative();
                    break;
            }
        } else {
            info.s = source.getResults().getData(s, TsData.class);
            info.mul = finals != null ? finals.getMode().isMultiplicative() : false;
        }
        if (info.s == null) {
            return null;
        }
        if (last > 0) {
            TsPeriodSelector selector = new TsPeriodSelector();
            selector.last(last * info.s.getFrequency().intValue());
            info.s = info.s.select(selector);
        }
        if (info.s.getLength() < 4 * info.s.getFrequency().intValue()) {
            return null;
        }
        return info;
    }

}
