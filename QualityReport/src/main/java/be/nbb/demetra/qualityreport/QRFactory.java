/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.qualityreport;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.sa.output.INbOutputFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jean Palate
 */
@ServiceProvider(service = INbOutputFactory.class,
position = 10025)
public class QRFactory implements INbOutputFactory{
    
    private QROutputConfiguration config=new QROutputConfiguration();

    @Override
    public AbstractOutputNode createNode() {
        return new QRNode(config);
    }

    @Override
    public String getName() {
        return QROutputFactory.NAME;
    }

    @Override
    public AbstractOutputNode createNodeFor(Object properties) {
        if (properties instanceof QROutputConfiguration)
            return new QRNode((QROutputConfiguration) properties);
        else
            return null;
    }
}
