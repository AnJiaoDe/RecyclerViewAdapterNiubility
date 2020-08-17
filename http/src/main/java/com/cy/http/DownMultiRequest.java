package com.cy.http;

import org.json.JSONObject;

import java.io.File;

public class DownMultiRequest extends Request {
    private String pathToSave;
    private File fileTemp;
//    private int count_thread;
    private long index_start;
    private long index_end;
    private JSONObject jsonObject_temp;

    DownMultiRequest(DownMultiBuilder builder) {
        super(builder);
        this.pathToSave=builder.getPathToSave();
        this.fileTemp = builder.getFileTemp();
//        this.count_thread = builder.getCount_thread();
        this.index_start = builder.getIndex_start();
        this.index_end = builder.getIndex_end();
        this.jsonObject_temp=builder.getJsonObject_temp();
    }

    public String getPathToSave() {
        return pathToSave;
    }

    public File getFileTemp() {
        return fileTemp;
    }

//    public int getCount_thread() {
//        return count_thread;
//    }

    public long getIndex_start() {
        return index_start;
    }

    public long getIndex_end() {
        return index_end;
    }

    public JSONObject getJsonObject_temp() {
        return jsonObject_temp;
    }

    public static class DownMultiBuilder extends Builder {
        private String pathToSave;
        private File fileTemp;
//        private int count_thread;

        private long index_start;
        private long index_end;
        private JSONObject jsonObject_temp;

        public String getPathToSave() {
            return pathToSave;
        }

        public DownMultiBuilder setPathToSave(String pathToSave) {
            this.pathToSave = pathToSave;
            return this;
        }

        public File getFileTemp() {
            return fileTemp;
        }

        public DownMultiBuilder setFileTemp(File fileTemp) {
            this.fileTemp = fileTemp;
            return this;

        }

//        public int getCount_thread() {
//            return count_thread;
//        }
//
//        public DownMultiBuilder setCount_thread(int count_thread) {
//            this.count_thread = count_thread;
//            return this;
//        }

        public long getIndex_start() {
            return index_start;
        }

        public DownMultiBuilder setIndex_start(long index_start) {
            this.index_start = index_start;
            return this;

        }

        public long getIndex_end() {
            return index_end;
        }

        public DownMultiBuilder setIndex_end(long index_end) {
            this.index_end = index_end;
            return this;
        }

        public JSONObject getJsonObject_temp() {
            return jsonObject_temp;
        }

        public DownMultiBuilder setJsonObject_temp(JSONObject jsonObject_temp) {
            this.jsonObject_temp = jsonObject_temp;
            return this;

        }

        @Override
        public DownMultiRequest build() {
            return new DownMultiRequest(this);
        }
    }
}
