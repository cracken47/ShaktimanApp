package com.alabhya.Shaktiman.models;

public class OtpResponse {
    private String message;

    private int status;

    private int otp;

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public int getStatus ()
    {
        return status;
    }

    public void setStatus (int status)
    {
        this.status = status;
    }
}
