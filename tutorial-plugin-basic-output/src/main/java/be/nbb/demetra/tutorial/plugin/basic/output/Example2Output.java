/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.basic.output;

import ec.satoolkit.ISaSpecification;
import ec.tss.sa.documents.SaDocument;
import ec.tss.sa.output.BasicConfiguration;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.IOutput;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsDataTable;
import ec.tstoolkit.utilities.Paths;
import java.io.File;
import java.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The output object will make the actual output work. At creation, it should
 * make a copy of the current configuration, to avoid unexpected changes.
 *
 * @author Jean Palate
 */
public class Example2Output implements IOutput<SaDocument<ISaSpecification>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(Example2OutputFactory.class);
    private final Example2Config config_;
    private File folder_;
    private int cur_;

    public Example2Output(Example2Config config) {
        config_ = config.clone();
    }

    @Override
    public String getName() {
        return Example2OutputFactory.NAME;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void process(SaDocument<ISaSpecification> document) throws Exception {
        String file = Paths.concatenate(folder_.getAbsolutePath(), config_.getFilePrefix() + cur_);
        file = Paths.changeExtension(file, "txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.append(document.getInput().getName());
            writer.append("\r\n");
            CompositeResults results = document.getResults();
            if (results != null) {
                writer.append("likelihood").append("\r\n");
                Double aic = results.getData("likelihood.aicc", Double.class);
                if (aic != null) {
                    writer.write(Double.toString(aic));
                    writer.append("\r\n");
                }
                Double bic = results.getData("likelihood.bic", Double.class);
                if (bic != null) {
                    writer.write(Double.toString(bic));
                    writer.append("\r\n");
                }
                writer.append("-----------------------------");
                writer.append("\r\n");
                TsDataTable main = new TsDataTable();
                main.insert(-1, results.getData("sa", TsData.class));
                main.insert(-1, results.getData("t", TsData.class));
                main.insert(-1, results.getData("s", TsData.class));
                main.insert(-1, results.getData("i", TsData.class));
                writer.write(main.toString());
            } else {
                writer.append("processing failed");
            }
            writer.close();
        }
        ++cur_;
    }

    @Override
    public void start(Object context) throws Exception {
        folder_ = BasicConfiguration.folderFromContext(config_.getFolder(), context);
        cur_ = 1;
    }

    @Override
    public void end(Object context) throws Exception {
        // nothing to do
    }

}
