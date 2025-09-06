package com.nhalamphitrentroi.testapp1.dto;

import java.util.List;

/**
 * DTO for log hint response with pagination
 */
public class LogHintResponse {
    private int status;
    private String errorcode;
    private String errordes;
    private int totalPages;
    private long totalElements;
    private List<LogHintItem> listLog;

    public LogHintResponse() {}

    public LogHintResponse(int status, String errorcode, String errordes, 
                          int totalPages, long totalElements, List<LogHintItem> listLog) {
        this.status = status;
        this.errorcode = errorcode;
        this.errordes = errordes;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.listLog = listLog;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrordes() {
        return errordes;
    }

    public void setErrordes(String errordes) {
        this.errordes = errordes;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<LogHintItem> getListLog() {
        return listLog;
    }

    public void setListLog(List<LogHintItem> listLog) {
        this.listLog = listLog;
    }

    /**
     * Inner class for log hint item
     */
    public static class LogHintItem {
        private String database_name;
        private String sql;
        private String suggestion;
        private boolean is_resolved;

        public LogHintItem() {}

        public LogHintItem(String database_name, String sql, String suggestion, boolean is_resolved) {
            this.database_name = database_name;
            this.sql = sql;
            this.suggestion = suggestion;
            this.is_resolved = is_resolved;
        }

        // Getters and Setters
        public String getDatabase_name() {
            return database_name;
        }

        public void setDatabase_name(String database_name) {
            this.database_name = database_name;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public boolean isIs_resolved() {
            return is_resolved;
        }

        public void setIs_resolved(boolean is_resolved) {
            this.is_resolved = is_resolved;
        }
    }
}
