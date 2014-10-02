/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or â€“ as soon they will be approved 
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
package be.nbb.demetra.random;

import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.utils.IParam;
import ec.tss.tsproviders.utils.Params;
import ec.tstoolkit.data.ReadDataBlock;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.sarima.SarimaSpecification;

/**
 *
 * @author Jean Palate
 * @author Philippe Charles
 */
public class RandomBean {

    static final IParam<DataSource, Integer> X_SEED = Params.onInteger(0, "seed");
    static final IParam<DataSource, Integer> X_LENGTH = Params.onInteger(240, "length");
    static final IParam<DataSource, Integer> X_P = Params.onInteger(0, "p");
    static final IParam<DataSource, Integer> X_D = Params.onInteger(1, "d");
    static final IParam<DataSource, Integer> X_Q = Params.onInteger(1, "q");
    static final IParam<DataSource, Integer> X_S = Params.onInteger(12, "s");
    static final IParam<DataSource, Integer> X_BP = Params.onInteger(0, "bp");
    static final IParam<DataSource, Integer> X_BD = Params.onInteger(1, "bd");
    static final IParam<DataSource, Integer> X_BQ = Params.onInteger(1, "bq");
    static final IParam<DataSource, double[]> X_COEFF = Params.onDoubleArray("coeff", -.8, -.6);
    static final IParam<DataSource, Integer> X_COUNT = Params.onInteger(100, "count");
    static final IParam<DataSource, Double> X_STD = Params.onDouble(0.0, "stde");
    //
    int seed;
    int length;
    int p, d, q, s, bp, bd, bq;
    // the number of coeff must be p+bp+q+bq !!!
    double[] coeff;
    double stde;
    int count;

    public RandomBean() {
        this.seed = X_SEED.defaultValue();
        this.length = X_LENGTH.defaultValue();
        this.p = X_P.defaultValue();
        this.d = X_D.defaultValue();
        this.q = X_Q.defaultValue();
        this.s = X_S.defaultValue();
        this.bp = X_BP.defaultValue();
        this.bd = X_BD.defaultValue();
        this.bq = X_BQ.defaultValue();
        this.coeff = X_COEFF.defaultValue();
        this.count = X_COUNT.defaultValue();
        this.stde = X_STD.defaultValue();
    }

    // GETTER/SETTERS >
    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getBp() {
        return bp;
    }

    public void setBp(int bp) {
        this.bp = bp;
    }

    public int getBd() {
        return bd;
    }

    public void setBd(int bd) {
        this.bd = bd;
    }

    public int getBq() {
        return bq;
    }

    public void setBq(int bq) {
        this.bq = bq;
    }

    public double[] getCoeff() {
        return coeff;
    }

    public void setCoeff(double[] coeff) {
        this.coeff = coeff;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getStde() {
        return this.stde;
    }

    public void setStde(double e) {
        this.stde = e;
    }
    // < GETTER/SETTERS

    public static RandomBean fromDataSource(DataSource dataSource) {
        RandomBean result = new RandomBean();
        result.seed = X_SEED.get(dataSource);
        result.length = X_LENGTH.get(dataSource);
        result.p = X_P.get(dataSource);
        result.d = X_D.get(dataSource);
        result.q = X_Q.get(dataSource);
        result.s = X_S.get(dataSource);
        result.bp = X_BP.get(dataSource);
        result.bd = X_BD.get(dataSource);
        result.bq = X_BQ.get(dataSource);
        result.coeff = X_COEFF.get(dataSource);
        result.count = X_COUNT.get(dataSource);
        result.stde = X_STD.get(dataSource);
        return result;
    }

    public DataSource toDataSource() {
        DataSource.Builder builder = DataSource.builder(RandomProvider.SOURCE, RandomProvider.VERSION);
        X_SEED.set(builder, seed);
        X_LENGTH.set(builder, length);
        X_P.set(builder, p);
        X_D.set(builder, d);
        X_Q.set(builder, q);
        X_S.set(builder, s);
        X_BP.set(builder, bp);
        X_BD.set(builder, bd);
        X_BQ.set(builder, bq);
        X_COEFF.set(builder, coeff);
        X_COUNT.set(builder, count);
        X_STD.set(builder, stde);
        return builder.build();
    }

    public SarimaSpecification toSpecification() {
        SarimaSpecification result = new SarimaSpecification(s);
        result.setP(p);
        result.setD(d);
        result.setQ(q);
        result.setBP(bp);
        result.setBD(bd);
        result.setBQ(bq);
        return result;
    }

    public SarimaModel toModel() {
        SarimaModel result = new SarimaModel(toSpecification());
        if (coeff != null) {
            result.setParameters(new ReadDataBlock(coeff));
        }
        return result;
    }
}
