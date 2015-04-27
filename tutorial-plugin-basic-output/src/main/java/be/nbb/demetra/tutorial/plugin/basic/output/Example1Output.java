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
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.utilities.Paths;
import java.io.FileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The output object will make the actual output work. At creation, it should
 * make a copy of the current configuration, to avoid unexpected changes.
 *
 * @author Jean Palate
 */
public class Example1Output implements IOutput<SaDocument<ISaSpecification>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(Example1OutputFactory.class);
    private final Example1Config config_;
    private FileWriter writer_;

    public Example1Output(Example1Config config) {
        config_ = config.clone();
    }

    @Override
    public String getName() {
        return Example1OutputFactory.NAME; 
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void process(SaDocument<ISaSpecification> document) throws Exception {
        // We append to the file some output for each series
        if (writer_ != null) {
            writer_.write(document.getInput().getName());
            CompositeResults results = document.getResults();
            if (results != null) {
                writer_.append('\t');
                Double aic = results.getData("likelihood.aicc", Double.class);
                if (aic != null) {
                    writer_.write(Double.toString(aic));
                }
                writer_.append('\t');
                Double bic = results.getData("likelihood.bicc", Double.class);
                if (bic != null) {
                    writer_.write(Double.toString(bic));
                }
                writer_.append('\t');
                Double ser = results.getData("residuals.ser-ml", Double.class);
                if (ser != null) {
                    writer_.write(Double.toString(ser));
                }
                writer_.append('\t');
                SarimaModel arima = results.getData("arima", SarimaModel.class);
                if (arima != null) {
                    writer_.write(arima.toString());
                }
            }
            writer_.append("\r\n");
        }
    }

    @Override
    public void start(Object context) throws Exception {
        // We create the file that will be used during the processing of all the series
        String folder = BasicConfiguration.folderFromContext(config_.getFolder().getAbsolutePath(), context);
        String file = Paths.concatenate(folder, config_.getFileName());
        file = Paths.changeExtension(file, "txt");
        writer_ = new FileWriter(file);
    }

    @Override
    public void end(Object context) throws Exception {
        // We close the file opend in the start method
        if (writer_ != null) {
            writer_.close();
        }
    }

}
