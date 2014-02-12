package be.nbb.demetra.various;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ec.util.chart.ColorScheme;
import static ec.util.chart.ColorSchemeSupport.rgb;
import ec.util.chart.impl.AbstractColorScheme;
import ec.util.chart.impl.BasicColor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 * A basic color scheme for charts.
 *
 * @author charphi
 */
@ServiceProvider(service = ColorScheme.class)
public class RGBColorScheme extends AbstractColorScheme {

    @Override
    public String getName() {
        return "RGB color scheme";
    }

    @Override
    public List<Integer> getAreaColors() {
        return Arrays.asList(BasicColor.RED, BasicColor.GREEN, BasicColor.BLUE);
    }

    @Override
    public Map<KnownColor, Integer> getAreaKnownColors() {
        return knownColors(
                BasicColor.BLUE,
                BasicColor.DARK_GRAY,
                BasicColor.GRAY,
                BasicColor.GREEN,
                rgb(255, 200, 0),
                BasicColor.RED,
                BasicColor.YELLOW);
    }

    @Override
    public int getBackColor() {
        return BasicColor.WHITE;
    }

    @Override
    public int getPlotColor() {
        return BasicColor.WHITE;
    }

    @Override
    public int getGridColor() {
        return BasicColor.LIGHT_GRAY;
    }
}
