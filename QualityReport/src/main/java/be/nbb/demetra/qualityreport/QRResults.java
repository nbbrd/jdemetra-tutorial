/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.qualityreport;

import ec.satoolkit.DecompositionMode;
import ec.satoolkit.ISaSpecification;
import ec.satoolkit.ISeriesDecomposition;
import ec.satoolkit.diagnostics.CombinedSeasonalityTest;
import ec.satoolkit.diagnostics.StationaryVarianceDecomposition;
import ec.satoolkit.seats.SeatsResults;
import ec.satoolkit.x11.X11Results;
import ec.tss.sa.documents.SaDocument;
import ec.tstoolkit.algorithm.IProcResults;
import ec.tstoolkit.algorithm.IProcSpecification;
import ec.tstoolkit.algorithm.ProcessingInformation;
import ec.tstoolkit.information.InformationMapper;
import ec.tstoolkit.modelling.ComponentInformation;
import ec.tstoolkit.modelling.ComponentType;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.modelling.arima.PreprocessingModel;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import ec.tstoolkit.timeseries.simplets.TsPeriod;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PCUser
 */
public class QRResults implements IProcResults {

    public static final String VAR_CYCLE = "var_cycle", VAR_SEAS = "var_seas",
            VAR_TD = "var_td", VAR_IRR = "var_irr", VAR_OTHER = "var_other", VAR_TOT = "var_total",
            START = "start", END = "end", N = "n", METHOD = "method", TEST1 = "test1", TEST2 = "test2";

    public static final String[] allItems() {
        return mapper.keys();
    }

//    public static final String[] ALL_VARS = new String[]{VAR_CYCLE, VAR_SEAS, 
//        VAR_IRR, VAR_TD, VAR_OTHER, VAR_TOT, N, START, END, 
//        METHOD, TEST1, TEST2};

    private StationaryVarianceDecomposition vardecomp;
    private int n;
    private String Method;
    private TsPeriod start, end;
    private CombinedSeasonalityTest test, test2;

    public <S extends ISaSpecification> QRResults(SaDocument<S> doc) {
        try {
            ISaSpecification specification = doc.getSpecification();
            Method = specification.toLongString();
            IProcResults results = doc.getResults();
            if (results != null) {
                PreprocessingModel pp = doc.getPreprocessingPart();
                IProcResults decomp = doc.getDecompositionPart();
                n = pp.description.getEstimationDomain().getLength();
                start = pp.description.getEstimationDomain().getStart();
                end = pp.description.getEstimationDomain().getLast();
                vardecomp = new StationaryVarianceDecomposition();
                vardecomp.process(doc.getResults());
                if (decomp instanceof SeatsResults) {
                    SeatsResults seats = (SeatsResults) decomp;
                    calccombined(seats.getSeriesDecomposition());
                } else if (decomp instanceof X11Results) {
                    X11Results x11 = (X11Results) decomp;
                    calccombined(x11.getSeriesDecomposition());
                }
                TsData si = decomp.getData(ModellingDictionary.SI_CMP, TsData.class);
                DecompositionMode mode = decomp.getData(ModellingDictionary.MODE, DecompositionMode.class);
                if (si != null && mode != null) {
                    calccombined(si, mode);
                }
            }
        } catch (Exception ex) {
        }
    }

    private void calccombined(ISeriesDecomposition output) {
        if (output == null)
            return;
        TsData s = output.getSeries(ComponentType.Seasonal, ComponentInformation.Value);
        TsData i = output.getSeries(ComponentType.Irregular, ComponentInformation.Value);
        TsData si;
        boolean mul = output.getMode() != DecompositionMode.Additive;
        if (mul) {
            si = TsData.multiply(s, i);
        } else {
            si = TsData.add(s, i);
        }
        test = new CombinedSeasonalityTest(si, mul);
    }

    private void calccombined(TsData si, DecompositionMode mode) {
        boolean mul = mode != DecompositionMode.Additive;
        test2 = new CombinedSeasonalityTest(si, mul);
    }

    @Override
    public boolean contains(String id) {
//        switch (id) {
//            case VAR_CYCLE:
//                return true;
//            case VAR_SEAS:
//                return true;
//            case VAR_IRR:
//                return true;
//            case VAR_TD:
//                return true;
//            case VAR_OTHER:
//                return true;
//            case VAR_TOT:
//                return true;
//        }
//        return false;
        synchronized (mapper) {
            return mapper.contains(id);
        }
    }

    @Override
    public Map<String, Class> getDictionary() {
//        HashMap<String, Class> dic = new HashMap<>();
//        dic.put(VAR_CYCLE, Double.class);
//        dic.put(VAR_SEAS, Double.class);
//        dic.put(VAR_TD, Double.class);
//        dic.put(VAR_IRR, Double.class);
//        dic.put(VAR_OTHER, Double.class);
//        dic.put(VAR_TOT, Double.class);
//        return dic;
        synchronized (mapper) {
            LinkedHashMap<String, Class> dictionary = new LinkedHashMap<>();
            mapper.fillDictionary(null, dictionary);
            return dictionary;
        }
    }

    @Override
    public <T> T getData(String id, Class<T> tclass) {
//        switch (id) {
//            case VAR_CYCLE:
//                return (T) (Double) vardecomp.getVarC();
//            case VAR_SEAS:
//                return (T) (Double) vardecomp.getVarS();
//            case VAR_IRR:
//                return (T) (Double) vardecomp.getVarI();
//            case VAR_TD:
//                return (T) (Double) vardecomp.getVarTD();
//            case VAR_OTHER:
//                return (T) (Double) vardecomp.getVarP();
//            case VAR_TOT:
//                return (T) (Double) vardecomp.getVarTotal();
//        }
//        return null;
        synchronized (mapper) {
            if (mapper.contains(id)) {
                return mapper.getData(this, id, tclass);
            } else {
                return null;
            }
        }
    }

    @Override
    public List<ProcessingInformation> getProcessingInformation() {
        return Collections.EMPTY_LIST;
    }

    public static void fillDictionary(String prefix, Map<String, Class> map) {
        mapper.fillDictionary(prefix, map);
    }

    // MAPPERS
    public static <T> void addMapping(String name, InformationMapper.Mapper<QRResults, T> mapping) {
        synchronized (mapper) {
            mapper.add(name, mapping);
        }
    }

    private static final InformationMapper<QRResults> mapper = new InformationMapper<>();

    static {
        mapper.add(VAR_CYCLE, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarC();
            }
        });
        mapper.add(VAR_SEAS, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarS();
            }
        });
        mapper.add(VAR_IRR, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarI();
            }
        });
        mapper.add(VAR_TD, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarTD();
            }
        });
        mapper.add(VAR_OTHER, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarP();
            }
        });
        mapper.add(VAR_TOT, new InformationMapper.Mapper<QRResults, Double>(Double.class) {
            @Override
            public Double retrieve(QRResults source) {
                if (source.vardecomp == null)
                    return null;
                return source.vardecomp.getVarTotal();
            }
        });
        mapper.add(METHOD, new InformationMapper.Mapper<QRResults, String>(String.class) {
            @Override
            public String retrieve(QRResults source) {
                return source.Method;
            }
        });
        mapper.add(START, new InformationMapper.Mapper<QRResults, TsPeriod>(TsPeriod.class) {
            @Override
            public TsPeriod retrieve(QRResults source) {
                return source.start;
            }
        });
        mapper.add(END, new InformationMapper.Mapper<QRResults, TsPeriod>(TsPeriod.class) {
            @Override
            public TsPeriod retrieve(QRResults source) {
                return source.end;
            }
        });
        mapper.add(N, new InformationMapper.Mapper<QRResults, Integer>(Integer.class) {
            @Override
            public Integer retrieve(QRResults source) {
                return source.n;
            }
        });
        mapper.add(TEST1, new InformationMapper.Mapper<QRResults, String>(String.class) {
            @Override
            public String retrieve(QRResults source) {
                if (source.test == null) {
                    return null;
                }
                return source.test.getSummary().toString();
            }
        });
        mapper.add(TEST2, new InformationMapper.Mapper<QRResults, String>(String.class) {
            @Override
            public String retrieve(QRResults source) {
                if (source.test2 == null) {
                    return null;
                }
                return source.test2.getSummary().toString();
            }
        });
    }
}
