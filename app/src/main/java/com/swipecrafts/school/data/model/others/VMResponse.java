package com.swipecrafts.school.data.model.others;

/**
 * Created by Madhusudan Sapkota on 4/12/2018.
 */
public class VMResponse<M> {

    private boolean status;

    private String message;

    private M data;

    public VMResponse() {
    }

    public VMResponse(boolean status, String message, M data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean status() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public M getData() {
        return data;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(M data) {
        this.data = data;
    }
}
