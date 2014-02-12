/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

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
import org.openide.windows.*;

/**
 *
 * @author charphi
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
            for (DStatItem o : DStatsHelper.ITEMS) {
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
}
