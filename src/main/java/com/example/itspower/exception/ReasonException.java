package com.example.itspower.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ReasonException extends RuntimeException {
    private  final Integer statusCode;
    private  final String causeMsg;
    private final  String detailsMessage;
    public ReasonException(ErrorCode errorCode) {
        super(errorCode.getDetailsMessage());
        this.statusCode = errorCode.getResponseStatus();
        this.causeMsg = errorCode.getCauseMsg();
        this.detailsMessage = errorCode.getDetailsMessage();
    }


    public ReasonException(Integer errorCode, String causeMsg, String detailsMessage) {
        super(detailsMessage);
        this.statusCode = errorCode;
        this.causeMsg = causeMsg;
        this.detailsMessage = detailsMessage;
    }


    public ReasonException(Integer statusCode, String causeMsg, Exception e) {
        this.statusCode = statusCode;
        this.causeMsg = causeMsg;
        this.detailsMessage = e.getMessage();
    }
}
