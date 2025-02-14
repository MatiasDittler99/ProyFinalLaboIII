package ar.edu.utn.frbb.tup.controller.handler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiError {
    private Integer errorCode;
    private String errorMessage;

    public CustomApiError(){}
    public CustomApiError(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public CustomApiError(String errorMessage, Integer errorCode){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

