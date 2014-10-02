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
package be.nbb.demetra.various;

import ec.util.chart.ColorScheme;
import static ec.util.chart.ColorSchemeSupport.rgb;
import ec.util.chart.impl.AbstractColorScheme;
import ec.util.chart.impl.BasicColor;
import static ec.util.chart.impl.BasicColor.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * A basic color scheme for charts.
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = ColorScheme.class)
public final class RGBColorScheme extends AbstractColorScheme {

    @Override
    public String getName() {
        return "RGB color scheme";
    }

    @Override
    public List<Integer> getAreaColors() {
        return Arrays.asList(RED, GREEN, BLUE);
    }

    @Override
    public Map<KnownColor, Integer> getAreaKnownColors() {
        int orange = rgb(255, 200, 0);
        return knownColors(BLUE, DARK_GRAY, GRAY, GREEN, orange, RED, YELLOW);
    }

    @Override
    public int getBackColor() {
        return BasicColor.WHITE;
    }

    @Override
    public int getPlotColor() {
        return WHITE;
    }

    @Override
    public int getGridColor() {
        return LIGHT_GRAY;
    }
}
