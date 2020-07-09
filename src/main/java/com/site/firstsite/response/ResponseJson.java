package com.site.firstsite.response;

public class ResponseJson {
    private Long id;
    private boolean statusBefore;
    private boolean statusAfter;

    public ResponseJson() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatusBefore() {
        return statusBefore;
    }

    public void setStatusBefore(boolean statusBefore) {
        this.statusBefore = statusBefore;
    }

    public boolean isStatusAfter() {
        return statusAfter;
    }

    public void setStatusAfter(boolean statusAfter) {
        this.statusAfter = statusAfter;
    }
}
