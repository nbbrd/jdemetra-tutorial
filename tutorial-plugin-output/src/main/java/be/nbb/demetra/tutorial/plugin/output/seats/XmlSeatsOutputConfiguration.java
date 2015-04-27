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

package be.nbb.demetra.tutorial.plugin.output.seats;

import ec.tss.sa.output.BasicConfiguration;
import ec.tss.sa.output.CsvLayout;
import ec.tstoolkit.utilities.Jdk6;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jean Palate & BAYENSK
 */
public class XmlSeatsOutputConfiguration implements Cloneable {
    
    public static final String NAME="seats_output";

    private File folder_ ;
    private String name_=NAME;

    public XmlSeatsOutputConfiguration() {
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

    @Override
    public XmlSeatsOutputConfiguration clone() {
        try {
            return (XmlSeatsOutputConfiguration) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
