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
package be.nbb.demetra.tutorial.plugin.output.seasonality;

import ec.satoolkit.ISaSpecification;
import ec.tss.formatters.CsvInformationFormatter;
import ec.tss.sa.documents.SaDocument;
import ec.tss.sa.output.BasicConfiguration;
import ec.tstoolkit.algorithm.IOutput;
import ec.tstoolkit.algorithm.IProcResults;
import ec.tstoolkit.utilities.NamedObject;
import ec.tstoolkit.utilities.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kristof Bayens
 */
public class CsvSeasonalityOutput implements IOutput<SaDocument<ISaSpecification>> {

    public static final Logger LOGGER = LoggerFactory.getLogger(CsvSeasonalityOutputFactory.class);
    private final CsvSeasonalityOutputConfiguration config_;
    private final List<NamedObject<IProcResults>> infos_ = new ArrayList<>();
    private File folder_;

    public CsvSeasonalityOutput(CsvSeasonalityOutputConfiguration config) {
        config_ = (CsvSeasonalityOutputConfiguration) config.clone();
    }

    @Override
    public void process(SaDocument<ISaSpecification> document) {
        infos_.add(new NamedObject<IProcResults>(document.getInput().getName(),
                new SeasonalityTestsResults(document, config_.getSeries(), config_.getTests(), config_.getLast())));
//       summary_.add(Jdk6.Collections.toArray(config_.getSeries(), String.class), document);
    }

    @Override
    public void start(Object context) {
        folder_ = BasicConfiguration.folderFromContext(config_.getFolder(), context);
    }

    @Override
    public void end(Object context) throws Exception {
        CsvInformationFormatter fmt = new CsvInformationFormatter();
        String file = Paths.concatenate(folder_.getAbsolutePath(), config_.getFileName());
        file = Paths.changeExtension(file, "csv");
        try (FileOutputStream matrix = new FileOutputStream(file)) {
            OutputStreamWriter writer = new OutputStreamWriter(matrix, StandardCharsets.ISO_8859_1);
            fmt.formatResults(writer, infos_, config_.getItems(config_.getDetail()), true);
        }
        infos_.clear();
    }

    @Override
    public String getName() {
        return CsvSeasonalityOutputFactory.NAME;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

}
