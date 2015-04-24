/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seasonality;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.sa.output.INbOutputFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jean Palate
 */
@ServiceProvider(service = INbOutputFactory.class,
position = 10000)
public class CsvSeasonalityFactory implements INbOutputFactory{
    
    private CsvSeasonalityOutputConfiguration config=new CsvSeasonalityOutputConfiguration();

    @Override
    public AbstractOutputNode createNode() {
        return new CsvSeasonalityNode(config);
    }

    @Override
    public String getName() {
        return CsvSeasonalityOutputFactory.NAME;
    }

    @Override
    public AbstractOutputNode createNodeFor(Object properties) {
        if (properties instanceof CsvSeasonalityOutputConfiguration)
            return new CsvSeasonalityNode((CsvSeasonalityOutputConfiguration) properties);
        else
            return null;
    }
}
