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
public class Example2Node extends AbstractOutputNode<Example2Config> {


    public Example2Node() {
        super(new Example2Config());
        setDisplayName(Example2OutputFactory.NAME);
    }
    
    public Example2Node(Example2Config config) {
        super(config);
        setDisplayName(Example2OutputFactory.NAME);
    }

    @Override
    protected Sheet createSheet() {
        Example2Config config = getLookup().lookup(Example2Config.class);
        Sheet sheet = super.createSheet();
        NodePropertySetBuilder builder = new NodePropertySetBuilder();
        
        builder.reset("Location");
        builder.withFile().select(config, "Folder").directories(true).description("Base output folder. Will be extended by the workspace and processing names").add();
        builder.with(String.class).select(config, "filePrefix").display("File prefix").add();
        sheet.put(builder.build());
 
        return sheet;
     }

    @Override
    public ISaOutputFactory getFactory() {
        return new Example2OutputFactory(getLookup().lookup(Example2Config.class));
    }
    
}
