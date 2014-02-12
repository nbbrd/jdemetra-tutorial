/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.random;

import ec.tss.*;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.utils.AbstractDataSourceLoader;
import ec.tss.tsproviders.utils.IParam;
import ec.tss.tsproviders.utils.Params;
import ec.tstoolkit.arima.ArimaModelBuilder;
import ec.tstoolkit.random.XorshiftRNG;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author palatej
 * @author charphi
 */
@ServiceProvider(service = ITsProvider.class)
public class RandomProvider extends AbstractDataSourceLoader<double[][], RandomBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomProvider.class);
    public static final String SOURCE = "RND";
    public static final String VERSION = "20121021";
    static final IParam<DataSet, Integer> Z_INDEX = Params.onInteger(0, "index");

    public RandomProvider() {
        super(LOGGER, SOURCE, TsAsyncMode.None);
    }

    @Override
    protected double[][] loadFromBean(RandomBean bean) throws Exception {
        // 1. create a random engine based on arima
        SarimaModel arima = bean.toModel();
        ArimaModelBuilder builder = new ArimaModelBuilder();
        builder.setRandomNumberGenerator(new XorshiftRNG(bean.getSeed()));
        // 2. generate data
        double[][] result = new double[bean.getCount()][];
        for (int i = 0; i < bean.getCount(); ++i) {
            result[i] = builder.generate(arima, bean.getLength());
        }
        return result;
    }

    @Override
    public String getDisplayName() {
        return "Random Arima generator";
    }

    @Override
    public List<DataSet> children(DataSource dataSource) throws IllegalArgumentException {
        support.check(dataSource);
        DataSet.Builder builder = DataSet.builder(dataSource, DataSet.Kind.SERIES);
        DataSet[] result = new DataSet[decodeBean(dataSource).getCount()];
        for (int i = 0; i < result.length; i++) {
            Z_INDEX.set(builder, i);
            result[i] = builder.build();
        }
        return Arrays.asList(result);
    }

    @Override
    public List<DataSet> children(DataSet parent) throws IllegalArgumentException {
        throw new IllegalArgumentException("No hierarchy");
    }

    @Override
    public String getDisplayName(DataSource dataSource) throws IllegalArgumentException {
        support.check(dataSource);
        return decodeBean(dataSource).toSpecification().toString();
    }

    @Override
    public String getDisplayName(DataSet dataSet) throws IllegalArgumentException {
        support.check(dataSet);
        return String.format("%s - %s", 
                getDisplayName(dataSet.getDataSource()), 
                getDisplayNodeName(dataSet));
    }

    @Override
    public String getDisplayNodeName(DataSet dataSet) throws IllegalArgumentException {
        support.check(dataSet);
        return Z_INDEX.get(dataSet).toString();
    }

    @Override
    protected void fillCollection(TsCollectionInformation info, DataSource dataSource) throws IOException {
        // get data
        double[][] rawData = cache.getUnchecked(dataSource);
        TsFrequency freq = TsFrequency.valueOf(decodeBean(dataSource).getS());
        // fill TsCollectionInformation
        int i = 0;
        for (DataSet dataSet : children(dataSource)) {
            TsInformation o = new TsInformation(getDisplayName(dataSet), toMoniker(dataSet), TsInformationType.Data);
            o.data = new TsData(freq, 2000, 0, rawData[i++], false);
            info.items.add(o);
        }
    }

    @Override
    protected void fillCollection(TsCollectionInformation info, DataSet dataSet) throws IOException {
        throw new IOException("No hierarchy");
    }

    @Override
    protected void fillSeries(TsInformation info, DataSet dataSet) throws IOException {
        // get data
        double[][] rawData = cache.getUnchecked(dataSet.getDataSource());
        TsFrequency freq = TsFrequency.valueOf(decodeBean(dataSet.getDataSource()).getS());
        // fill TsInformation
        int index = Z_INDEX.get(dataSet);
        info.name = getDisplayName(dataSet);
        info.data = new TsData(freq, 2000, 0, rawData[index], false);
        info.type = TsInformationType.Data;
    }

    @Override
    public RandomBean newBean() {
        return new RandomBean();
    }

    @Override
    public DataSource encodeBean(Object bean) throws IllegalArgumentException {
        return ((RandomBean) bean).toDataSource();
    }

    @Override
    public RandomBean decodeBean(DataSource dataSource) throws IllegalArgumentException {
        return RandomBean.fromDataSource(dataSource);
    }
}
