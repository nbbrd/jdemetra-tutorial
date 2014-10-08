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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jean Palate
 */
public class MyDiagnosticsConfiguration implements Cloneable {

    public static final double SEV = .001, BAD = .01, UNC = .05;

    private double sev_ = SEV, bad_ = BAD, unc_ = UNC;
    private boolean enabled_ = true;

    @Override
    public MyDiagnosticsConfiguration clone() {
        try {
            return (MyDiagnosticsConfiguration) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public double getSevereThreshold() {
        return sev_;
    }

    public double getBadThreshold() {
        return bad_;
    }

    public double getUncertainThreshold() {
        return unc_;
    }

    public boolean isEnabled() {
        return enabled_;
    }

    public void setEnabled(boolean enabled) {
        enabled_ = enabled;
    }
}
