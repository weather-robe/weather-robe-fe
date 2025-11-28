package com.cookandroid.weatherrobe.common;

public class CommonApiResponse<S> {
    private String resultType;
    private Object error;
    private S success;

    public String getResultType() { return resultType; }
    public Object getError() { return error; }
    public S getSuccess() { return success; }

    public boolean isSuccessful() {
        return "SUCCESS".equals(resultType) && success != null;
    }
}
