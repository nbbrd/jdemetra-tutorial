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
package be.nbb.demetra.tutorial.plugin.output.seats;

import com.google.common.base.Throwables;
import ec.satoolkit.ISaSpecification;
import ec.satoolkit.seats.SeatsResults;
import ec.tss.sa.documents.SaDocument;
import ec.tss.sa.output.BasicConfiguration;
import ec.tstoolkit.algorithm.IOutput;
import ec.tstoolkit.algorithm.IProcResults;
import ec.tstoolkit.utilities.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jean Palate
 */
public class XmlSeatsOutput implements IOutput<SaDocument<ISaSpecification>> {

    static final JAXBContext XML_SEATS_DETAILS_CONTEXT;

    static {
        try {
            XML_SEATS_DETAILS_CONTEXT = JAXBContext.newInstance(XmlSeatsDetails.class);
        } catch (JAXBException ex) {
            throw Throwables.propagate(ex);
        }
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(XmlSeatsOutputFactory.class);
    private final XmlSeatsOutputConfiguration config_;
    private final List<SeatsSummary> summary_ = new ArrayList<>();
    private File folder_;

    public XmlSeatsOutput(XmlSeatsOutputConfiguration config) {
        config_ = (XmlSeatsOutputConfiguration) config.clone();
    }

    @Override
    public void process(SaDocument<ISaSpecification> document) {
        IProcResults decomp = document.getDecompositionPart();
        SeatsResults seats;
        if (decomp instanceof SeatsResults) {
            seats = (SeatsResults) decomp;
        } else {
            seats = null;
        }
        summary_.add(new SeatsSummary(document.getInput().getName(), seats));
    }

    @Override
    public void start(Object context) {
        folder_ = BasicConfiguration.folderFromContext(config_.getFolder(), context);
    }

    @Override
    public void end(Object context) throws Exception {
        String nfile = config_.getFileName();
        nfile = Paths.changeExtension(nfile, "xml");
        
        XmlSeatsDetails xml = new XmlSeatsDetails();
        xml.seatsResults = new XmlSeatsDetail[summary_.size()];
        int cur = 0;
        for (SeatsSummary scur : summary_) {
            xml.seatsResults[cur++] = XmlSeatsDetail.of(scur);
        }
        File file = new File(folder_, nfile);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {

                Marshaller marshaller = XML_SEATS_DETAILS_CONTEXT.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(xml, writer);
                writer.flush();
                LOGGER.info(nfile);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

        summary_.clear();
    }

    @Override
    public String getName() {
        return XmlSeatsOutputFactory.NAME;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

}
