/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seats;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.sa.output.INbOutputFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jean Palate
 */
@ServiceProvider(service = INbOutputFactory.class,
position = 20000)
public class XmlSeatsFactory implements INbOutputFactory{
    
    private XmlSeatsOutputConfiguration config=new XmlSeatsOutputConfiguration();

    @Override
    public AbstractOutputNode createNode() {
        return new XmlSeatsNode(config);
    }

    @Override
    public String getName() {
        return XmlSeatsOutputFactory.NAME;
    }

    @Override
    public AbstractOutputNode createNodeFor(Object properties) {
        if (properties instanceof XmlSeatsOutputConfiguration)
            return new XmlSeatsNode((XmlSeatsOutputConfiguration) properties);
        else
            return null;
    }
}
