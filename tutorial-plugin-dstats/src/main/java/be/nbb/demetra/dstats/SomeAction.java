/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import ec.nbdemetra.ui.tsproviders.CollectionNode;
import ec.nbdemetra.ui.tsproviders.DataSetNode;
import ec.nbdemetra.ui.tsproviders.DataSourceNode;
import ec.nbdemetra.ui.tsproviders.SeriesNode;
import ec.satoolkit.algorithm.implementation.TramoSeatsProcessingFactory;
import ec.satoolkit.tramoseats.TramoSeatsSpecification;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.TsInformationType;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.TsProviders;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.Parameter;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.data.DescriptiveStatistics;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsDataTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
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
            int N = 120;
            try (OutputWriter out = DStatsHelper.CHECK.getOut()) {
                for (Ts s : ts) {
                    //out.println(MultiLineNameUtil.last(s.getName()));
                    TramoSeatsSpecification spec = TramoSeatsSpecification.RSA5.clone();
                    spec.getTramoSpecification().setUsingAutoModel(false);
                    TsData data = s.getTsData();
                    if (data != null) {
                        double[] th = new double[N];
                        double[] bth = new double[N];

                        for (int i = 0; i < N; ++i) {
                            try {
                                TsData datac = data.drop(i, N - i);
                                CompositeResults sa = TramoSeatsProcessingFactory.process(datac, spec);
                                Parameter p = sa.getData("arima.th(1)", Parameter.class);
                                if (p != null) {
                                    th[i] = p.getValue();
                                }
                                p = sa.getData("arima.bth(1)", Parameter.class);
                                if (p != null) {
                                    bth[i] = p.getValue();
                                }
                            } catch (Exception err) {
                                out.println("not processed");
                            }
                        }
                        for (int i = 0; i < N; ++i) {
                            if (i != 0) {
                                out.print('\t');
                            }
                            out.print(th[i]);
                        }
                        out.println();
                        for (int i = 0; i < N; ++i) {
                            if (i != 0) {
                                out.print('\t');
                            }
                            out.print(bth[i]);
                        }
                        out.println();
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
}
