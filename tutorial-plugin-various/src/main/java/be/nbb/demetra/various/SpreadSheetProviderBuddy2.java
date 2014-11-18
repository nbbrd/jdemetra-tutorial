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
package be.nbb.demetra.various;

import com.google.common.base.Optional;
import ec.tss.tsproviders.spreadsheet.engine.SpreadSheetSeries;
import ec.nbdemetra.spreadsheet.SpreadsheetProviderBuddy;
import ec.nbdemetra.ui.tsproviders.IDataSourceProviderBuddy;
import ec.tss.TsMoniker;
import ec.tss.tsproviders.DataSet;
import ec.tss.tsproviders.DataSource;
import ec.tss.tsproviders.IDataSourceProvider;
import ec.tss.tsproviders.TsProviders;
import ec.tss.tsproviders.spreadsheet.SpreadSheetBean;
import ec.tss.tsproviders.spreadsheet.SpreadSheetProvider;
import static ec.util.chart.impl.TangoColorScheme.ALUMINIUM6;
import static ec.util.chart.impl.TangoColorScheme.DARK_PLUM;
import static ec.util.chart.impl.TangoColorScheme.DARK_SCARLET_RED;
import static ec.util.chart.impl.TangoColorScheme.DARK_SKY_BLUE;
import static ec.util.chart.impl.TangoColorScheme.LIGHT_SKY_BLUE;
import static ec.util.chart.swing.SwingColorSchemeSupport.rgbToColor;
import static ec.util.chart.swing.SwingColorSchemeSupport.withAlpha;
import static ec.util.various.swing.FontAwesome.FA_ELLIPSIS_H;
import static ec.util.various.swing.FontAwesome.FA_ELLIPSIS_V;
import static ec.util.various.swing.FontAwesome.FA_FOLDER;
import static ec.util.various.swing.FontAwesome.FA_FOLDER_OPEN;
import static ec.util.various.swing.FontAwesome.FA_TH;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Image;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.EnumMap;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = IDataSourceProviderBuddy.class, supersedes = {"ec.nbdemetra.spreadsheet.SpreadsheetProviderBuddy"})
public class SpreadSheetProviderBuddy2 extends SpreadsheetProviderBuddy {

    @Override
    public Image getIcon(int type, boolean opened) {
        return getOrCreate(type).provider;
    }

    @Override
    public Image getIcon(DataSource dataSource, int type, boolean opened) {
        return getOrCreate(type).dataSource;
    }

    @Override
    public Image getIcon(DataSet dataSet, int type, boolean opened) {
        Icons icons = getOrCreate(type);
        switch (dataSet.getKind()) {
            case COLLECTION:
                return opened ? icons.collectionOpen : icons.collection;
            case SERIES:
                SpreadSheetSeries series = getSeries(dataSet);
                if (series != null && series.data.isPresent()) {
                    switch (series.alignType) {
                        case VERTICAL:
                            return icons.seriesVertical;
                        case HORIZONTAL:
                            return icons.seriesHorizontal;
                    }
                } else {
                    return icons.seriesEmpty;
                }
                break;
        }
        return super.getIcon(dataSet, type, opened);
    }

    @Override
    public Image getIcon(TsMoniker moniker, int type, boolean opened) {
        DataSet dataSet = toDataSet(moniker);
        return dataSet != null ? getIcon(dataSet, type, opened) : super.getIcon(moniker, type, opened);
    }

    @Override
    public boolean editBean(String title, Object bean) throws IntrospectionException {
        SpreadSheetBeanPanel v = new SpreadSheetBeanPanel();
        v.loadBean((SpreadSheetBean) bean);

        DialogDescriptor dd = v.createDialogDescriptor(title);

        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setIconImages(FA_TH.getImages(rgbToColor(ALUMINIUM6), 16f, 32f, 64f));
        dialog.setVisible(true);

        if (dd.getValue() == NotifyDescriptor.OK_OPTION) {
            v.storeBean((SpreadSheetBean) bean);
            return true;
        }
        return false;
    }

    private static DataSet toDataSet(TsMoniker moniker) {
        Optional<IDataSourceProvider> provider = TsProviders.lookup(IDataSourceProvider.class, moniker);
        if (provider.isPresent()) {
            DataSet dataSet = provider.get().toDataSet(moniker);
            if (dataSet != null) {
                return dataSet;
            }
        }
        return null;
    }

    private SpreadSheetSeries getSeries(DataSet dataSet) {
        try {
            return TsProviders.lookup(SpreadSheetProvider.class, SpreadSheetProvider.SOURCE).get().getSeries(dataSet);
        } catch (IOException ex) {
            return null;
        }
    }

    private final EnumMap<BeanIconType, Icons> cache = new EnumMap<>(BeanIconType.class);

    private Icons getOrCreate(int type) {
        BeanIconType key = BeanIconType.parseInt(type);
        Icons result = cache.get(key);
        if (result == null) {
            result = new Icons(key);
            cache.put(key, result);
        }
        return result;
    }

    private static final class Icons {

        final Image provider;
        final Image dataSource;
        final Image collection;
        final Image collectionOpen;
        final Image seriesVertical;
        final Image seriesHorizontal;
        final Image seriesEmpty;

        private static final Color PROVIDER_COLOR = rgbToColor(ALUMINIUM6);
        private static final Color SOURCE_COLOR = rgbToColor(DARK_SKY_BLUE);
        private static final Color COLLECTION_COLOR = rgbToColor(LIGHT_SKY_BLUE);
        private static final Color SERIES_BACK_COLOR = withAlpha(rgbToColor(LIGHT_SKY_BLUE), 150);
        private static final Color SERIES_FRONT_COLOR = rgbToColor(DARK_PLUM);
        private static final Color SERIES_EMPTY_COLOR = withAlpha(rgbToColor(DARK_SCARLET_RED), 150);

        Icons(BeanIconType type) {
            float size = toSize(type);
            this.provider = FA_TH.getImage(PROVIDER_COLOR, size);
            this.dataSource = FA_TH.getImage(SOURCE_COLOR, size);
            this.collection = FA_FOLDER.getImage(COLLECTION_COLOR, size);
            this.collectionOpen = FA_FOLDER_OPEN.getImage(COLLECTION_COLOR, size);
            Image back = FA_TH.getImage(SERIES_BACK_COLOR, size);
            Image frontV = FA_ELLIPSIS_V.getImage(SERIES_FRONT_COLOR, size);
            Image frontH = FA_ELLIPSIS_H.getImage(SERIES_FRONT_COLOR, size);
            this.seriesVertical = ImageUtilities.mergeImages(back, frontV, 0, 0);
            this.seriesHorizontal = ImageUtilities.mergeImages(back, frontH, 0, 0);
            this.seriesEmpty = FA_TH.getImage(SERIES_EMPTY_COLOR, size);
        }

        private static float toSize(BeanIconType type) {
            return 14f;
        }
    }

    private enum BeanIconType {

        COLOR_16x16(1),
        COLOR_32x32(2),
        MONO_16x16(3),
        MONO_32x32(4),
        UNKNOWN(-1);

        private final int value;

        private BeanIconType(int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }

        public static BeanIconType parseInt(int value) {
            for (BeanIconType o : values()) {
                if (o.value == value) {
                    return o;
                }
            }
            return UNKNOWN;
        }
    }
}
