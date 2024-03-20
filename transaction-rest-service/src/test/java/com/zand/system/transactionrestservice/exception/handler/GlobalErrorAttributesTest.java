package com.zand.system.transactionrestservice.exception.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GlobalErrorAttributesTest {

    @Test
    public void testGetErrorAttributes() {
        // Create a mock ServerRequest object
        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/v1/accounts/123456");

        // Create a mock Throwable object
        Throwable throwable = new Throwable("Test error message");
        when(serverRequest.attribute(anyString())).thenReturn(Optional.of(throwable));

        // Create a new GlobalErrorAttributes object
        GlobalErrorAttributes globalErrorAttributes = new GlobalErrorAttributes();

        // Call the getErrorAttributes method and verify the response
        Map<String, Object> errorAttributes = globalErrorAttributes.getErrorAttributes(serverRequest, ErrorAttributeOptions.defaults());
        assertEquals("Test error message", errorAttributes.get("message"));
        assertEquals("/v1/accounts/123456", errorAttributes.get("endpoint url "));
    }
}
