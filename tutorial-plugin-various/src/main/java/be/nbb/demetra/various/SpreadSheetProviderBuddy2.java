/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.various;

import ec.nbdemetra.spreadsheet.SpreadsheetProviderBuddy;
import ec.nbdemetra.ui.tsproviders.IDataSourceProviderBuddy;
import ec.tss.tsproviders.spreadsheet.SpreadSheetBean;
import java.awt.Dialog;
import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author charphi
 */
@ServiceProvider(service = IDataSourceProviderBuddy.class, supersedes = {"ec.nbdemetra.spreadsheet.SpreadsheetProviderBuddy"})
public class SpreadSheetProviderBuddy2 extends SpreadsheetProviderBuddy {

    @Override
    public Image getIcon(int type, boolean opened) {
        return ImageUtilities.loadImage("/smiley.png", true);
    }

    @Override
    public boolean editBean(String title, Object bean) throws IntrospectionException {

        SpreadSheetBeanPanel v = new SpreadSheetBeanPanel();
        v.loadBean((SpreadSheetBean)bean);
        
        Image image = getIcon(BeanInfo.ICON_COLOR_16x16, false);

        DialogDescriptor dd = v.createDialogDescriptor(title);
        
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        if (image != null) {
            dialog.setIconImage(image);
        }
        dialog.setVisible(true);

        if (dd.getValue() == NotifyDescriptor.OK_OPTION) {
            v.storeBean((SpreadSheetBean)bean);
            return true;
        }
        return false;
    }
    
}
