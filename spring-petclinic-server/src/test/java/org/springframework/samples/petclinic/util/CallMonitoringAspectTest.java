package org.springframework.samples.petclinic.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallMonitoringAspectTest {

    private CallMonitoringAspect aspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        aspect = new CallMonitoringAspect();
    }

    @Test
    void testSetEnabledAndIsEnabled() {
        assertTrue(aspect.isEnabled());
        
        aspect.setEnabled(false);
        assertFalse(aspect.isEnabled());
        
        aspect.setEnabled(true);
        assertTrue(aspect.isEnabled());
    }

    @Test
    void testReset() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn("result");
        
        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);
        
        assertTrue(aspect.getCallCount() > 0);
        assertTrue(aspect.getCallTime() >= 0);
        
        aspect.reset();
        
        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void testGetCallCount() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn("result");
        
        assertEquals(0, aspect.getCallCount());
        
        aspect.invoke(joinPoint);
        assertEquals(1, aspect.getCallCount());
        
        aspect.invoke(joinPoint);
        assertEquals(2, aspect.getCallCount());
        
        aspect.invoke(joinPoint);
        assertEquals(3, aspect.getCallCount());
    }

    @Test
    void testGetCallTimeWithNoInvocations() {
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void testGetCallTimeWithInvocations() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenAnswer(invocation -> {
            Thread.sleep(10);
            return "result";
        });
        
        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);
        
        long callTime = aspect.getCallTime();
        assertTrue(callTime >= 0);
    }

    @Test
    void testInvokeWhenEnabled() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn("expectedResult");
        
        aspect.setEnabled(true);
        Object result = aspect.invoke(joinPoint);
        
        assertEquals("expectedResult", result);
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void testInvokeWhenDisabled() throws Throwable {
        when(joinPoint.proceed()).thenReturn("expectedResult");
        
        aspect.setEnabled(false);
        Object result = aspect.invoke(joinPoint);
        
        assertEquals("expectedResult", result);
        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
        verify(joinPoint, times(1)).proceed();
        verify(joinPoint, never()).toShortString();
    }

    @Test
    void testInvokeWhenExceptionThrown() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));
        
        aspect.setEnabled(true);
        
        assertThrows(RuntimeException.class, () -> aspect.invoke(joinPoint));
        
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void testInvokeMultipleTimes() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn("result");
        
        aspect.setEnabled(true);
        
        for (int i = 0; i < 5; i++) {
            aspect.invoke(joinPoint);
        }
        
        assertEquals(5, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void testInvokeWithDifferentResults() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("testMethod");
        when(joinPoint.proceed()).thenReturn("result1", "result2", null);
        
        aspect.setEnabled(true);
        
        assertEquals("result1", aspect.invoke(joinPoint));
        assertEquals("result2", aspect.invoke(joinPoint));
        assertNull(aspect.invoke(joinPoint));
        
        assertEquals(3, aspect.getCallCount());
    }
}