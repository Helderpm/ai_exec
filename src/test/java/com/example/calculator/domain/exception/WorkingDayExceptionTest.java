package com.example.calculator.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkingDayExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test error message";
        
        WorkingDayException exception = new WorkingDayException(expectedMessage);
        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        String expectedMessage = "Test error message";
        Throwable expectedCause = new RuntimeException("Root cause");
        
        WorkingDayException exception = new WorkingDayException(expectedMessage, expectedCause);
        
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }

    @Test
    @DisplayName("Should have correct exception type")
    void shouldHaveCorrectExceptionType() {
        WorkingDayException exception = new WorkingDayException("Test message");
        
        assertEquals(WorkingDayException.class, exception.getClass());
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Should preserve null message")
    void shouldPreserveNullMessage() {
        WorkingDayException exception = new WorkingDayException( null);
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should preserve null cause")
    void shouldPreserveNullCause() {
        WorkingDayException exception = new WorkingDayException("Test message", null);
        
        assertNotNull(exception);
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should handle empty message")
    void shouldHandleEmptyMessage() {
        String emptyMessage = "";
        
        WorkingDayException exception = new WorkingDayException(emptyMessage);
        
        assertNotNull(exception);
        assertEquals(emptyMessage, exception.getMessage());
    }
}
