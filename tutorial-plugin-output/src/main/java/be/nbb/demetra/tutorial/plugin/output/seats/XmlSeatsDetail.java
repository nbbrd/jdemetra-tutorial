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

import ec.tss.xml.arima.XmlArimaModel;
import ec.tstoolkit.arima.ArimaModel;
import ec.tstoolkit.ucarima.UcarimaModel;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Kristof Bayens
 */
@XmlRootElement(name = XmlSeatsDetail.RNAME)
@XmlType(name = XmlSeatsDetail.NAME)
public class XmlSeatsDetail {

    static final String NAME = "seatsDetailType";
    static final String RNAME = "seatsDetail";

    @XmlAttribute
    public String name;
    @XmlElement
    public XmlArimaModel model;
    @XmlElement
    public XmlArimaModel sa;
    @XmlElement
    public XmlArimaModel trend;
    @XmlElement
    public XmlArimaModel seasonal;
    @XmlElement
    public XmlArimaModel transitory;
    @XmlElement
    public XmlArimaModel irregular;
    @XmlElement
    public Double ser;
    @XmlElement
    public Boolean mean;
    @XmlAttribute
    public Boolean changed;
    @XmlAttribute
    public Boolean cutoff;

    public static XmlSeatsDetail of(SeatsSummary info) {
        XmlSeatsDetail xml = new XmlSeatsDetail();
        xml.name = info.getName();
        if (info.isValid()) {
            UcarimaModel ucm = info.getUcarima();
            xml.model = new XmlArimaModel();
            xml.model.copy(ArimaModel.create(ucm.getModel()));
            ArimaModel cmp = ucm.getComponent(0);
            if (!cmp.isNull()) {
                xml.trend = new XmlArimaModel();
                xml.trend.copy(cmp);
            }
            cmp = ucm.getComponent(1);
            if (!cmp.isNull()) {
                xml.seasonal = new XmlArimaModel();
                xml.seasonal.copy(cmp);
                xml.sa = new XmlArimaModel();
                xml.sa.copy(ucm.getComplement(1));
            }
            cmp = ucm.getComponent(2);
            if (!cmp.isNull()) {
                xml.transitory = new XmlArimaModel();
                xml.transitory.copy(cmp);
            }
            cmp = ucm.getComponent(3);
            if (!cmp.isNull()) {
                xml.irregular = new XmlArimaModel();
                xml.irregular.copy(cmp);
            }
            xml.ser = info.getSer();
            xml.mean = info.isMeanCorrection();
            xml.changed = info.isChanged();
            xml.cutoff = info.isCutOff();
        }
        return xml;
    }
}
