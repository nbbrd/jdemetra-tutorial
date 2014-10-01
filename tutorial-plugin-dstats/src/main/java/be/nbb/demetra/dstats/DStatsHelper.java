/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or â€“ as soon they will be approved 
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
 * @author Philippe Charles
 */
final class DStatsHelper {

    static final InputOutput IO = IOProvider.getDefault().getIO("Stats", new Action[]{new OpenSettings()});
    static final List<DStatsItem> ITEMS = Lists.newArrayList(DStatsItem.DATA_COUNT, DStatsItem.MISSING_VALUES, DStatsItem.STDEV);

    private static final class OpenSettings extends AbstractAction {

        public OpenSettings() {
            putValue(SMALL_ICON, ImageUtilities.loadImageIcon("/be/nbb/demetra/dstats/preferences-system.png", false));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OptionsDisplayer.getDefault().open(DStatsOptionsPanelController.ID);
        }
    }
}
