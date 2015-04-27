/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.basic.output;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.sa.output.INbOutputFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 * Service provider for the NetBeans platform
 * @author Jean Palate
 */
@ServiceProvider(service = INbOutputFactory.class,
position = 100000)
public class Example1Factory implements INbOutputFactory{
    
    /**
     * Default configuration
     */
    private Example1Config config=new Example1Config();

    @Override
    public AbstractOutputNode createNode() {
        return new Example1Node(config);
    }

    @Override
    public String getName() {
        return Example1OutputFactory.NAME;
    }

    @Override
    public AbstractOutputNode createNodeFor(Object properties) {
        if (properties instanceof Example1Config)
            return new Example1Node((Example1Config) properties);
        else
            return null;
    }
}
