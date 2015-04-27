/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.basic.output;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.ui.properties.NodePropertySetBuilder;
import ec.tss.sa.ISaOutputFactory;
import org.openide.nodes.Sheet;

/**
 * For NetBeans only.
 * Represents the node in the list of the output.
 * It should provide the properties of the underlying configuration
 * @author Jean Palate
 */
public class Example1Node extends AbstractOutputNode<Example1Config> {


    public Example1Node() {
        super(new Example1Config());
        setDisplayName(Example1OutputFactory.NAME);
    }
    
    public Example1Node(Example1Config config) {
        super(config);
        setDisplayName(Example1OutputFactory.NAME);
    }

    @Override
    protected Sheet createSheet() {
        Example1Config config = getLookup().lookup(Example1Config.class);
        Sheet sheet = super.createSheet();
        NodePropertySetBuilder builder = new NodePropertySetBuilder();
        
        builder.reset("Location");
        builder.withFile().select(config, "Folder").directories(true).description("Base output folder. Will be extended by the workspace and processing names").add();
        builder.with(String.class).select(config, "fileName").display("File name").add();
        sheet.put(builder.build());
 
        return sheet;
     }

    @Override
    public ISaOutputFactory getFactory() {
        return new Example1OutputFactory(getLookup().lookup(Example1Config.class));
    }
    
}
