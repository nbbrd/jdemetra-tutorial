/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import ec.nbdemetra.ui.DemetraUI;
import ec.tss.Ts;
import org.openide.windows.OutputEvent;
import org.openide.windows.OutputListener;

/**
 * When clicked, it opens a TimeSeries with the default TsAction.
 *
 * @author charphi
 */
class TsHyperLinkListener implements OutputListener {

    final Ts ts;

    TsHyperLinkListener(Ts ts) {
        this.ts = ts;
    }

    @Override
    public void outputLineSelected(OutputEvent ev) {
    }

    @Override
    public void outputLineAction(OutputEvent ev) {
        DemetraUI.getInstance().getTsAction().open(ts);
    }

    @Override
    public void outputLineCleared(OutputEvent ev) {
    }
}
