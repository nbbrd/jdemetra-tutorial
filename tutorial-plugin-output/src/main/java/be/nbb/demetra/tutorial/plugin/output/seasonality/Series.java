/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seasonality;

import ec.nbdemetra.ui.properties.ListSelectionEditor;
import ec.tss.sa.output.BasicConfiguration;
import java.util.Arrays;

public class Series extends ListSelectionEditor<String> {

    public Series() {
        super(Arrays.asList(CsvSeasonalityOutputConfiguration.allSeries));
    }
}
