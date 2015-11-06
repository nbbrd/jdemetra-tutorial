/*
 * Copyright 2013 National Bank of Belgium
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
package be.nbb.demetra.qualityreport;

import ec.tss.sa.output.BasicConfiguration;
import ec.tss.sa.output.CsvLayout;
import ec.tstoolkit.modelling.ModellingDictionary;
import ec.tstoolkit.utilities.Jdk6;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jean Palate & BAYENSK
 */
public class QROutputConfiguration implements Cloneable {

    public static final String NAME = "quality", SEP = " - ";

     private int last_ = 10, det_ = 3;
     private File folder_;
    private String name_ = NAME;

    public QROutputConfiguration() {
    }

    public File getFolder() {
        return folder_;
    }

    public void setFolder(File value) {
        folder_ = value;
    }

    public String getFileName() {
        return name_;
    }

    public void setFileName(String value) {
        name_ = value;
    }

//    public List<String> getTests() {
//        return Arrays.asList(tests_);
//    }
//
//    public List<String> getSeries() {
//        return Arrays.asList(series_);
//    }
//
//    public void setTests(List<String> t) {
//        tests_ = new String[t.size()];
//        t.toArray(tests_);
//    }
//
//    public void setSeries(List<String> s) {
//        series_ = new String[s.size()];
//        s.toArray(series_);
//    }
//
//    public int getLast() {
//        return last_;
//    }
//
//    public void setLast(int l) {
//        last_ = l;
//    }

    public int getDetail() {
        return det_;
    }

    public void setDetail(int l) {
        if (l >= 0 && l <= 3) {
            det_ = l;
        }
    }

    static String item(String s, String test) {
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        builder.append(SEP);
        builder.append(test);
        return builder.toString();
    }

    static String item(String s, String test, int det) {
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        builder.append(SEP);
        builder.append(test);
        if (det > 0) {
            builder.append(':').append(det);
        }
        return builder.toString();
    }


    @Override
    public QROutputConfiguration clone() {
        try {
            QROutputConfiguration clone = (QROutputConfiguration) super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
