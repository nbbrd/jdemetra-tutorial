/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seasonality;

import ec.nbdemetra.ui.properties.ListSelectionEditor;
import java.util.Arrays;

/**
 *
 * @author Jean Palate
 */
public class Tests extends ListSelectionEditor<String> {

    public Tests() {
        super(Arrays.asList(CsvSeasonalityOutputConfiguration.allTests));
    }
}