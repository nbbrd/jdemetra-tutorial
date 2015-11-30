/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seats;

import ec.nbdemetra.sa.output.AbstractOutputNode;
import ec.nbdemetra.ui.properties.NodePropertySetBuilder;
import ec.tss.sa.ISaOutputFactory;
import org.openide.nodes.Sheet;

/**
 *
 * @author Jean Palate
 */
public class XmlSeatsNode extends AbstractOutputNode<XmlSeatsOutputConfiguration> {


    public XmlSeatsNode() {
        super(new XmlSeatsOutputConfiguration());
        setDisplayName(XmlSeatsOutputFactory.NAME);
    }
    
    public XmlSeatsNode(XmlSeatsOutputConfiguration config) {
        super(config);
        setDisplayName(XmlSeatsOutputFactory.NAME);
    }

    @Override
    protected Sheet createSheet() {
        XmlSeatsOutputConfiguration config = getLookup().lookup(XmlSeatsOutputConfiguration.class);
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
        return new XmlSeatsOutputFactory(getLookup().lookup(XmlSeatsOutputConfiguration.class));
    }
    
}
