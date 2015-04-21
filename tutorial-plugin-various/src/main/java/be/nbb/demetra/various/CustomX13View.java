/*
 * Copyright 2015 National Bank of Belgium
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

import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tss.sa.documents.X13Document;
import ec.tstoolkit.algorithm.IProcDocument;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.utilities.Id;
import ec.tstoolkit.utilities.LinearId;
import ec.ui.view.tsprocessing.IProcDocumentView;
import ec.ui.view.tsprocessing.ProcDocumentItemFactory;
import java.util.Collections;
import javax.swing.JComponent;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = ProcDocumentItemFactory.class, position = 1)
public final class CustomX13View extends ProcDocumentItemFactory {

    @Override
    public Class<? extends IProcDocument> getDocumentType() {
        return X13Document.class;
    }

    @Override
    public Id getItemId() {
        return new LinearId("Custom X13 view");
    }

    @Override
    public JComponent getView(IProcDocumentView<? extends IProcDocument> host, IProcDocument doc) throws IllegalArgumentException {
        X13Document x13Document = (X13Document) doc;
        TsData data = x13Document.getMStatistics().getIc();
        Ts ic = TsFactory.instance.createTs("ic", null, data);
        return host.getToolkit().getChart(Collections.singleton(ic));
    }
}
