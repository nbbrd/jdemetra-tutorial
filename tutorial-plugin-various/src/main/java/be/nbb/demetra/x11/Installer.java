package be.nbb.demetra.x11;

import ec.tss.Ts;
import ec.tss.sa.documents.X13Document;
import ec.tstoolkit.utilities.Id;
import ec.tstoolkit.utilities.InformationExtractor;
import ec.tstoolkit.utilities.LinearId;
import ec.ui.view.tsprocessing.DefaultItemUI;
import ec.ui.view.tsprocessing.IProcDocumentView;
import java.util.Collections;
import javax.swing.JComponent;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author palatej
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Id id = new LinearId("Specifications", "Original series");

        ec.nbdemetra.x13.ui.X13ViewFactory.getDefault().register(id, new InformationExtractor<X13Document, Ts>() {
            @Override
            public Ts retrieve(X13Document source) {
                return source.getTs(); 
            }

            @Override
            public void flush(X13Document source) {
            }
        },
                new DefaultItemUI<IProcDocumentView<X13Document>, Ts>() {
            @Override
            public JComponent getView(IProcDocumentView<X13Document> host, Ts information) {
                return host.getToolkit().getChart(Collections.singleton(information));
            }
        });
        
        // add differenced series
        
    }
}
