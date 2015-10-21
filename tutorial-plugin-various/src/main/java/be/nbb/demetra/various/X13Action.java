/*
 * Copyright 2013-2014 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
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

import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.actions.AbstractViewAction;
import ec.nbdemetra.x13.X13DocumentManager;
import ec.nbdemetra.x13.X13TopComponent;
import ec.satoolkit.x11.Mstatistics;
import ec.tss.sa.documents.X13Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "SaProcessing",
        id = "be.nbb.demetra.various.X13Action"
)
@ActionRegistration(
        displayName = "#CTL_X13Action", lazy = false)
@ActionReferences({
    @ActionReference(path = X13DocumentManager.ITEMPATH, position = 9000),
    @ActionReference(path = X13DocumentManager.CONTEXTPATH, position = 9000)
})
@Messages("CTL_X13Action=My X13 action")
public final class X13Action extends AbstractViewAction<X13TopComponent> {

    public X13Action() {
        super(X13TopComponent.class);
        putValue(NAME, Bundle.CTL_X13Action());
   }

    @Override
    protected void refreshAction() {
        enabled = false;
        X13TopComponent cur = context();
        if (cur != null) {
            enabled = true;
        }
    }

    @Override
    public boolean isEnabled() {
        refreshAction();
        return enabled;
    }

    @Override
    protected void process(X13TopComponent cur) {
        WorkspaceItem<X13Document> document = cur.getDocument();
        if (document != null) {
            X13Document element = document.getElement();
            Mstatistics ms = element.getMStatistics();
            if (ms != null) {
                javax.swing.JOptionPane.showMessageDialog(cur, Double.toString(ms.getQ()));
            }
        }
    }

}
