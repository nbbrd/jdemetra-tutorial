/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.dstats;

import ec.tstoolkit.data.DescriptiveStatistics;

/**
 * Lists all available statistics and provides a command to retrieve it.
 *
 * @author charphi
 */
public enum DStatItem {

    AVERAGE {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getAverage();
        }

        @Override
        public String getDisplayName() {
            return "Average";
        }
    },
    DATA_COUNT {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getDataCount();
        }

        @Override
        public String getDisplayName() {
            return "Data count";
        }
    }, KURTOSIS {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getKurtosis();
        }

        @Override
        public String getDisplayName() {
            return "Kurtosis";
        }
    }, MAX {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getMax();
        }

        @Override
        public String getDisplayName() {
            return "Max";
        }
    }, MEDIAN {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getMedian();
        }

        @Override
        public String getDisplayName() {
            return "Median";
        }
    }, MIN {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getMin();
        }

        @Override
        public String getDisplayName() {
            return "Min";
        }
    }, MISSING_VALUES {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getMissingValuesCount();
        }

        @Override
        public String getDisplayName() {
            return "Missing values";
        }
    }, OBS {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getObservationsCount();
        }

        @Override
        public String getDisplayName() {
            return "Obs";
        }
    }, RMSE {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getRmse();
        }

        @Override
        public String getDisplayName() {
            return "Rmse";
        }
    }, SKEWNESS {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getSkewness();
        }

        @Override
        public String getDisplayName() {
            return "Skewness";
        }
    }, STDEV {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getStdev();
        }

        @Override
        public String getDisplayName() {
            return "Stdev";
        }
    }, SUM {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getSum();
        }

        @Override
        public String getDisplayName() {
            return "Sum";
        }
    }, SUM_SQUARE {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getSumSquare();
        }

        @Override
        public String getDisplayName() {
            return "Sum square";
        }
    }, VAR {
        @Override
        public Number getValue(DescriptiveStatistics stats) {
            return stats.getVar();
        }

        @Override
        public String getDisplayName() {
            return "Var";
        }
    };

    public abstract Number getValue(DescriptiveStatistics stats);

    public abstract String getDisplayName();
}
