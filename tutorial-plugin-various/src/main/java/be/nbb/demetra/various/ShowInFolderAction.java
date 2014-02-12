package be.nbb.demetra.various;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import ec.nbdemetra.ui.nodes.SingleNodeAction;
import ec.nbdemetra.ui.tsproviders.DataSourceNode;
import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.TsProviders;
import ec.util.desktop.Desktop;
import ec.util.desktop.DesktopManager;
import java.io.File;
import java.io.IOException;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * Launch the default file manager and select the file used as a source by the
 * selected {@link DataSource}.<p> This action is enabled if these two
 * conditions are satisfied: <ul><li>the {@link Desktop.Action.SHOW_IN_FOLDER}
 * action is supported <li>the selected {@link DataSource} uses a file as source
 * </ul>
 *
 * @see Desktop#showInFolder(java.io.File)
 * @see TsProviders#getFile(ec.tss.tsproviders.DataSource)
 * @author charphi
 */
@ActionID(category = "Edit", id = "be.nbb.demetra.various.ShowInFolderAction")
@ActionRegistration(displayName = "#CTL_ShowInFolderAction")
@ActionReferences({
    @ActionReference(path = DataSourceNode.ACTION_PATH, position = 1701, separatorBefore = 1700)
})
@Messages("CTL_ShowInFolderAction=Show in folder")
public class ShowInFolderAction extends SingleNodeAction<DataSourceNode> {

    static final boolean SUPPORTED = DesktopManager.get().isSupported(Desktop.Action.SHOW_IN_FOLDER);

    public ShowInFolderAction() {
        super(DataSourceNode.class);
    }

    @Override
    protected void performAction(DataSourceNode activatedNode) {
        try {
            DesktopManager.get().showInFolder(getFile(activatedNode));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    protected boolean enable(DataSourceNode activatedNode) {
        return SUPPORTED && getFile(activatedNode) != null;
    }

    @Override
    public String getName() {
        return Bundle.CTL_ShowInFolderAction();
    }

    static File getFile(DataSourceNode activatedNode) {
        return TsProviders.getFile(activatedNode.getLookup().lookup(DataSource.class));
    }
}
