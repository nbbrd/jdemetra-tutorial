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
package be.nbb.demetra.dstats;

import ec.nbdemetra.ui.DemetraUI;
import ec.nbdemetra.ui.tsproviders.SeriesNode;
import ec.tss.Ts;
import ec.tss.TsInformationType;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.TsProviders;
import ec.tstoolkit.data.DescriptiveStatistics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;
import org.openide.windows.OutputWriter;

/**
 *
 * @author Philippe Charles
 */
@ActionID(category = "Edit", id = "be.nbb.demetra.dstats.DStatsAction")
@ActionRegistration(displayName = "#CTL_DStatsAction")
@ActionReferences({
    @ActionReference(path = SeriesNode.ACTION_PATH, position = 1600, separatorBefore = 1300)
})
@Messages("CTL_DStatsAction=Descriptive Stats")
public class DStatsAction implements ActionListener {

    private final SeriesNode context;

    public DStatsAction(SeriesNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            // 1. get data
            Ts ts = TsProviders.getTs(context.getLookup().lookup(DataSet.class), TsInformationType.Data).get();
            DescriptiveStatistics stats = new DescriptiveStatistics(ts.getTsData().getValues());

            // 2. get output window
            DStatsHelper.IO.select();
            OutputWriter out = DStatsHelper.IO.getOut();

            // 3. output header + hyperlink
            String title = ts.getName() + " (" + ts.getMoniker().getSource() + ")";
            out.println(title, new TsHyperLinkListener(ts));

            // 4. output details
            for (DStatsItem o : DStatsHelper.ITEMS) {
                out.print(o.getDisplayName());
                out.print(": ");
                out.print(o.getValue(stats));
                out.println();
            }
            //out.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * When clicked, it opens a TimeSeries with the default TsAction.
     */
    static final class TsHyperLinkListener implements OutputListener {

        private final Ts ts;

        public TsHyperLinkListener(Ts ts) {
            this.ts = ts;
        }

        @Override
        public void outputLineSelected(OutputEvent ev) {
        }

        @Override
        public void outputLineAction(OutputEvent ev) {
            DemetraUI.getDefault().getTsAction().open(ts);
        }

        @Override
        public void outputLineCleared(OutputEvent ev) {
        }
    }
}
