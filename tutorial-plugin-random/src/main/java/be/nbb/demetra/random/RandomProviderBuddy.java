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
package be.nbb.demetra.random;

import com.google.common.collect.Lists;
import ec.nbdemetra.ui.properties.FormattedPropertyEditor;
import ec.nbdemetra.ui.properties.NodePropertySetBuilder;
import ec.nbdemetra.ui.tsproviders.AbstractDataSourceProviderBuddy;
import ec.nbdemetra.ui.tsproviders.IDataSourceProviderBuddy;
import ec.tss.tsproviders.utils.Formatters;
import ec.tss.tsproviders.utils.Parsers;
import java.awt.Image;
import java.text.ParseException;
import java.util.List;
import javax.swing.JFormattedTextField;
import org.openide.nodes.Sheet.Set;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = IDataSourceProviderBuddy.class)
public class RandomProviderBuddy extends AbstractDataSourceProviderBuddy {

    @Override
    public String getProviderName() {
        return RandomProvider.SOURCE;
    }

    @Override
    public Image getIcon(int type, boolean opened) {
        // this overrides the default icon
        return ImageUtilities.loadImage("/document-binary.png", false);
    }

    @Override
    protected List<Set> createSheetSets(Object bean) {
        List<Set> result = Lists.newArrayList();

        NodePropertySetBuilder b = new NodePropertySetBuilder();

        b.reset("Arima");
        b.withInt()
                .select(bean, "p")
                .min(0).max(3)
                .display("Regular auto-regressive order (p)")
                .add();
        b.withInt()
                .select(bean, "d")
                .min(0).max(2)
                .display("Regular differencing order (d)")
                .add();
        b.withInt()
                .select(bean, "q")
                .min(0).max(3)
                .display("Regular moving average order (q)")
                .add();
        b.withInt()
                .select(bean, "bp")
                .min(0).max(1)
                .display("Seasonal auto-regressive order (bp)")
                .add();
        b.withInt()
                .select(bean, "bd")
                .min(0).max(1)
                .display("Seasonal differencing order (bd)")
                .add();
        b.withInt()
                .select(bean, "bq")
                .min(0).max(1)
                .display("Seasonal moving average order (bq)")
                .add();
        b.with(double[].class)
                .select(bean, "coeff")
                .editor(FormattedPropertyEditor.class)
                .attribute(FormattedPropertyEditor.FORMATTER_ATTRIBUTE, new DoubleArrayFormatter())
                .display("Coeff")
                .add();
        b.withDouble()
                .select(bean, "stde")
                .min(0.0).max(1)
                .display("Stde of coeff")
                .add();
        result.add(b.build());

        b.reset("Other");
        b.withInt().select(bean, "seed").display("Random seed").add();
        b.withInt().select(bean, "s").display("Frequency").add();
        b.withInt().select(bean, "length").display("Number of periods").add();
        b.withInt().select(bean, "count").display("Number of series").add();
        result.add(b.build());

        return result;
    }

    /**
     * Class used to convert double[] from/to String in a JFormattedTextField.
     */
    static class DoubleArrayFormatter extends JFormattedTextField.AbstractFormatter {

        @Override
        public Object stringToValue(String text) throws ParseException {
            return text == null ? new double[0] : Parsers.doubleArrayParser().parse(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            return value == null ? "" : Formatters.doubleArrayFormatter().formatAsString((double[]) value);
        }
    }
}
