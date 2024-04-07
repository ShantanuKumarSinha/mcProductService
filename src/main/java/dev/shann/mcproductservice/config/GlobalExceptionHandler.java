package dev.shann.mcproductservice.config;

import dev.shann.mcproductservice.utility.ProductNotFoundException;
import dev.shann.mcproductservice.utility.UnAuthorizedAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value ={UnAuthorizedAccessException.class})
    public ResponseEntity<Object> handleUnAuthorizedAccessException(
            UnAuthorizedAccessException unAuthorizedAccessException, WebRequest request) {
        return new ResponseEntity<>(
                unAuthorizedAccessException.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<Object> handleProductNotFoundException(
            ProductNotFoundException productNotFoundException, WebRequest request) {
        return new ResponseEntity<>(
                productNotFoundException.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


}
