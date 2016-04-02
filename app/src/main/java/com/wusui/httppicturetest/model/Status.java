package com.wusui.httppicturetest.model;

import java.util.List;

/**
 * Created by fg on 2016/3/24.
 */
public class Status {
    private String error;
    private List<results> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<results> getResults() {
        return results;
    }

    public void setResults(List<results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Status [error=" + error + ",result =" + results + "]";
    }

    public class results {
        private String id;
        private String ns;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private String used;
        private String who;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNs() {
            return ns;
        }

        public void setNs(String ns) {
            this.ns = ns;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public String toString() {
            return "results [_id=" + id + ", _ns=" + ns + ", createdAt=" + createdAt +
                    ", desc=" + desc + ",publishedAt=" + publishedAt + ", source=" + source + ", type" +
                    type + ", url=" + url + ", used=" + used + ", who" + who + "]";
        }
    }
}
