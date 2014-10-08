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

import ec.tss.sa.ISaDiagnosticsFactory;
import ec.tss.sa.SaManager;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.IDiagnostics;

/**
 *
 * @author Jean Palate
 */
public class MyDiagnosticsFactory implements ISaDiagnosticsFactory {
    
    static final String NAME="New Seasonality tests", DESC="New Seasonality tests";

    private MyDiagnosticsConfiguration config_;

    public MyDiagnosticsFactory() {
        config_ = new MyDiagnosticsConfiguration();
    }

    public MyDiagnosticsFactory(MyDiagnosticsConfiguration config) {
        config_ = config;
    }

    @Override
    public Scope getScope() {
        return Scope.Final; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getOrder() {
        return 0; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public boolean isEnabled() {
        return config_.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        config_.setEnabled(enabled);
    }

    @Override
    public Object getProperties() {
        return config_.clone();
    }

    @Override
    public void setProperties(Object obj) {
        if (obj instanceof MyDiagnosticsConfiguration) {
            MyDiagnosticsConfiguration nconfig = (MyDiagnosticsConfiguration) obj;
            config_ = nconfig.clone();
        }
    }

    @Override
    public IDiagnostics create(CompositeResults rslts) {
        return MyDiagnostics.create(rslts, config_);
    }

}
