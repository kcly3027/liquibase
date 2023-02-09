package liquibase.logging.mdc.customobjects;

import liquibase.logging.mdc.CustomMdcObject;

public class UpdateSummary implements CustomMdcObject {
    private String value;
    private int run;
    private int runPreviously;
    private Skipped skipped;
    private int totalChangesets;

    /**
     * Constructor for service locator.
     */
    public UpdateSummary() {
    }

    public UpdateSummary(String value, int run, int runPreviously, Skipped skipped, int totalChangesets) {
        this.value = value;
        this.run = run;
        this.runPreviously = runPreviously;
        this.skipped = skipped;
        this.totalChangesets = totalChangesets;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getRunPreviously() {
        return runPreviously;
    }

    public void setRunPreviously(int runPreviously) {
        this.runPreviously = runPreviously;
    }

    public Skipped getSkipped() {
        return skipped;
    }

    public void setSkipped(Skipped skipped) {
        this.skipped = skipped;
    }

    public int getTotalChangesets() {
        return totalChangesets;
    }

    public void setTotalChangesets(int totalChangesets) {
        this.totalChangesets = totalChangesets;
    }

    public static class Skipped {
        private int dbmsUnknown;
        private int labels;
        private int context;
        private int totalSkipped;

        /**
         * Constructor for service locator.
         */
        public Skipped() {
        }

        public Skipped(int dbmsUnknown, int labels, int context, int totalSkipped) {
            this.dbmsUnknown = dbmsUnknown;
            this.labels = labels;
            this.context = context;
            this.totalSkipped = totalSkipped;
        }

        public int getDbmsUnknown() {
            return dbmsUnknown;
        }

        public void setDbmsUnknown(int dbmsUnknown) {
            this.dbmsUnknown = dbmsUnknown;
        }

        public int getLabels() {
            return labels;
        }

        public void setLabels(int labels) {
            this.labels = labels;
        }

        public int getContext() {
            return context;
        }

        public void setContext(int context) {
            this.context = context;
        }

        public int getTotalSkipped() {
            return totalSkipped;
        }

        public void setTotalSkipped(int totalSkipped) {
            this.totalSkipped = totalSkipped;
        }

        public void incrementDbmsUnknown() {
            dbmsUnknown++;
        }

        public void incrementLabels() {
            labels++;
        }

        public void incrementContext() {
            context++;
        }
    }
}
