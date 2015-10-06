/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import ec.nbdemetra.ui.tsproviders.CollectionNode;
import ec.nbdemetra.ui.tsproviders.DataSourceNode;
import ec.nbdemetra.ui.tsproviders.SeriesNode;
import ec.satoolkit.algorithm.implementation.TramoSeatsProcessingFactory;
import ec.satoolkit.tramoseats.TramoSeatsSpecification;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.TsFactory;
import ec.tss.TsInformationType;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.TsProviders;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.modelling.arima.CheckLast;
import ec.tstoolkit.modelling.arima.tramo.TramoSpecification;
import ec.tstoolkit.timeseries.regression.OutlierType;
import ec.tstoolkit.timeseries.simplets.TsData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.OutputWriter;

@ActionID(
        category = "Edit",
        id = "be.nbb.demetra.dstats.SomeAction"
)
@ActionRegistration(
        displayName = "#CTL_SomeAction"
)
@ActionReferences({
    @ActionReference(path = CollectionNode.ACTION_PATH, position = 1620, separatorBefore = 1300),
    @ActionReference(path = SeriesNode.ACTION_PATH, position = 1620, separatorBefore = 1300),
    @ActionReference(path = DataSourceNode.ACTION_PATH, position = 1620, separatorBefore = 1300),})
@Messages("CTL_SomeAction=Control")
public final class SomeAction implements ActionListener {

    private final AbstractNode context;

    public SomeAction(AbstractNode context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        try {
            // 1. get data
            TsCollection ts = null;
            DataSet set = context.getLookup().lookup(DataSet.class);
            if (set != null) {
                ts = TsProviders.getTsCollection(set, TsInformationType.Data).get();
            } else {
                DataSource source = context.getLookup().lookup(DataSource.class);
                if (source != null) {
                    ts = TsProviders.getTsCollection(source, TsInformationType.Data).get();
                }
            }
            if (ts == null) {
                return;
            }
            ts.load(TsInformationType.All);
            DStatsHelper.CHECK.select();
            TramoSpecification myspec = TramoSpecification.TRfull.clone();
            myspec.getOutliers().remove(OutlierType.TC);
            CheckLast cl = new CheckLast(myspec.build());
            try (OutputWriter out = DStatsHelper.CHECK.getOut()) {
                for (Ts s : ts) {
//                    out.println(last(s.getName()));
                    if (cl.check(s.getTsData())) {
                        double score = cl.getScore(0);
                        if (Math.abs(score) > 4) {
//                            out.print(last(s.getName()));
                            CompositeResults saprocess = TramoSeatsProcessingFactory.process(s.getTsData(), TramoSeatsSpecification.RSAfull);
                            TsData sa = saprocess.getData("sa", TsData.class);
                            Ts ssa = TsFactory.instance.createTs(s.getName(), null, sa);
                            out.println(last(s.getName()), new DStatsAction.TsHyperLinkListener(ssa));
                            out.print('\t');
                            out.println(score);
                        }
                    } else {
                        out.println("not processed");
                    }
                    out.flush();
                }
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

	
	// copy of MultiLineName#last(String) to be remove with v2.1
    private static String last(String input) {
        int index = input.lastIndexOf("\n");
        return index == -1 ? input : input.substring(index + 1);
    }	
}
