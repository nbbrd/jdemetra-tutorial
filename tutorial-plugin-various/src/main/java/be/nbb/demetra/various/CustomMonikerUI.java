/*
 * Copyright 2013 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
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
package be.nbb.demetra.various;

import com.google.common.base.Optional;
import ec.nbdemetra.ui.MonikerUI;
import ec.nbdemetra.ui.tsproviders.DataSourceProviderBuddySupport;
import ec.nbdemetra.ui.tsproviders.IDataSourceProviderBuddy;
import ec.tss.Ts;
import ec.tss.TsMoniker;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.IDataSourceProvider;
import ec.tss.tsproviders.TsProviders;
import java.beans.BeanInfo;
import javax.swing.Icon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = MonikerUI.class, supersedes = "ec.nbdemetra.ui.MonikerUI")
public class CustomMonikerUI extends MonikerUI {

    @Override
    public Icon getIcon(Ts ts) {
        return getIcon(ts.getMoniker());
    }

    @Override
    public Icon getIcon(TsMoniker moniker) {
        DataSet dataSet = toDataSet(moniker);
        return dataSet != null ? getIcon(dataSet) : super.getIcon(moniker);
    }

    @Override
    public Icon getIcon(DataSet dataSet) {
        IDataSourceProviderBuddy buddy = DataSourceProviderBuddySupport.getDefault().get(dataSet);
        return ImageUtilities.image2Icon(buddy.getIcon(dataSet, BeanInfo.ICON_COLOR_16x16, false));
    }

    private DataSet toDataSet(TsMoniker moniker) {
        Optional<IDataSourceProvider> provider = TsProviders.lookup(IDataSourceProvider.class, moniker);
        if (provider.isPresent()) {
            DataSet dataSet = provider.get().toDataSet(moniker);
            if (dataSet != null) {
                return dataSet;
            }
        }
        return null;
    }
}
