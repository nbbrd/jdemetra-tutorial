/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.options.OptionsDisplayer;
import org.openide.util.ImageUtilities;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author charphi
 */
public final class DStatsHelper {

    static final InputOutput IO = IOProvider.getDefault().getIO("Stats", new Action[]{new OpenSettingsAction()});
    static final List<DStatItem> ITEMS = Lists.newArrayList(DStatItem.DATA_COUNT, DStatItem.MISSING_VALUES, DStatItem.STDEV);

    static class OpenSettingsAction extends AbstractAction {

        public OpenSettingsAction() {
            putValue(SMALL_ICON, ImageUtilities.loadImageIcon("/be/nbb/demetra/dstats/preferences-system.png", false));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OptionsDisplayer.getDefault().open(Demo1OptionsPanelController.ID);
        }
    }
}
