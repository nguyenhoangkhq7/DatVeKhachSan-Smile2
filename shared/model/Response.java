package model;

public class Response<T> {
    private boolean success;
    private T data;

    public Response() {}

    public Response(boolean success) {
        this.success = success;
    }

    public Response(boolean success, T data) {
        this.success = success;
        this.data = data;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
