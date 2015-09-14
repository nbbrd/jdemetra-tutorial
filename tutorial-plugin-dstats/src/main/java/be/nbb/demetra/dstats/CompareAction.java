/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import ec.nbdemetra.ui.tsproviders.SeriesNode;
import ec.satoolkit.algorithm.implementation.TramoSeatsProcessingFactory;
import ec.satoolkit.algorithm.implementation.X13ProcessingFactory;
import ec.satoolkit.tramoseats.TramoSeatsSpecification;
import ec.satoolkit.x13.X13Specification;
import ec.tss.Ts;
import ec.tss.TsInformationType;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.TsProviders;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.timeseries.TsPeriodSelector;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.OutputWriter;

@ActionID(
        category = "Edit",
        id = "be.nbb.demetra.dstats.CompareAction"
)
@ActionRegistration(
        displayName = "#CTL_CompareAction"
)
@ActionReferences({
    @ActionReference(path = SeriesNode.ACTION_PATH, position = 1650, separatorBefore = 1610)
})
@Messages("CTL_CompareAction=Compare SA specifications")
public final class CompareAction implements ActionListener {

    private final SeriesNode context;

    public CompareAction(SeriesNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Ts ts = TsProviders.getTs(context.getLookup().lookup(DataSet.class), TsInformationType.Data).get();
        ts.load(TsInformationType.Data);
        TsData s=ts.getTsData();
        if (s == null)
            return;
//        TramoSeatsSpecification[] allts = TramoSeatsSpecification.allSpecifications();
//        CompositeResults[] trs = new CompositeResults[allts.length];
//        for (int i = 0; i < trs.length; ++i) {
//            trs[i] = TramoSeatsProcessingFactory.process(s, allts[i]);
//        }
//        X13Specification[] allx = X13Specification.allSpecifications();
//        CompositeResults[] xs = new CompositeResults[allx.length];
//        for (int i = 0; i < xs.length; ++i) {
//            xs[i] = X13ProcessingFactory.process(s, allx[i]);
//        }
        // 2. get output window
        DStatsHelper.COMPARE.select();
        // 3. output header
        try (OutputWriter out = DStatsHelper.COMPARE.getOut()) {
            // 3. output header
            String title = ts.getName() + " (" + ts.getMoniker().getSource() + ")";
            out.println(title);
            int freq=s.getFrequency().intValue();
            TsPeriodSelector sel=new TsPeriodSelector();
            sel.last(freq);
            out.println(s.pctVariation(freq).select(sel));
            
//            for (int i=0; i<trs.length; ++i){
//                out.print(allts[i]);
//                out.print(('\t'));
//                out.println(trs[i].getData("likelihood.bicc", Double.class));
//            }
//            for (int i=0; i<xs.length; ++i){
//                out.print(allx[i]);
//                out.print(('\t'));
//                out.println(xs[i].getData("likelihood.bicc", Double.class));
//            }
        }
    }
}
