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

import ec.nbdemetra.ui.nodes.SingleNodeAction;
import ec.nbdemetra.ws.WorkspaceFactory;
import ec.nbdemetra.ws.WorkspaceItem;
import ec.nbdemetra.ws.actions.AbstractViewAction;
import ec.nbdemetra.ws.nodes.ItemWsNode;
import ec.nbdemetra.x13.X13DocumentManager;
import ec.nbdemetra.x13.X13TopComponent;
import ec.satoolkit.x11.Mstatistics;
import ec.tss.sa.documents.X13Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "SaProcessing",
        id = "be.nbb.demetra.various.X13ActionBis"
)
@ActionRegistration(
        displayName = "#CTL_X13ActionBis", lazy = false)
@ActionReferences({
    @ActionReference(path = X13DocumentManager.ITEMPATH, position = 9000)
})
@Messages("CTL_X13ActionBis=My X13 action bis")
public final class X13ActionBis extends SingleNodeAction<ItemWsNode> {

    public X13ActionBis() {
        super(ItemWsNode.class);
    }

    @Override
    protected void performAction(ItemWsNode context) {
        WorkspaceItem<?> cur = context.getItem();
        if (cur.getElement() instanceof X13Document) {
            X13Document doc = (X13Document) cur.getElement();
            if (doc != null) {
                Mstatistics ms = doc.getMStatistics();
                if (ms != null) {
                    NotifyDescriptor nd = new NotifyDescriptor.Message(Double.toString(ms.getQ()));
                    DialogDisplayer.getDefault().notify(nd);
                }
            }
        }
    }

    @Override
    protected boolean enable(ItemWsNode context
    ) {
        WorkspaceItem<?> cur = context.getItem();
        if (cur.getElement() instanceof X13Document) {
            X13Document doc = (X13Document) cur.getElement();
            return doc.getMStatistics() != null;
        }
        return false;
    }

    @Override
    public String getName() {
        return Bundle.CTL_X13ActionBis();
    }
 }
