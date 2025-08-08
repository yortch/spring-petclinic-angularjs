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

    @Mock
    private ProceedingJoinPoint joinPoint;

    private CallMonitoringAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new CallMonitoringAspect();
        // Use lenient stubbing because some tests in this class do not invoke the aspect
        // and therefore would otherwise trigger UnnecessaryStubbingException.
        lenient().when(joinPoint.toShortString()).thenReturn("jp");
    }

    @Test
    void isEnabled_defaultTrue_and_canToggle() {
        assertTrue(aspect.isEnabled());
        aspect.setEnabled(false);
        assertFalse(aspect.isEnabled());
        aspect.setEnabled(true);
        assertTrue(aspect.isEnabled());
    }

    @Test
    void getCallTime_whenNoCalls_returnsZero() {
        assertEquals(0, aspect.getCallCount());
        assertEquals(0L, aspect.getCallTime());
    }

    @Test
    void invoke_enabled_success_incrementsCallCount_andReturnsValue() throws Throwable {
        aspect.setEnabled(true);
        when(joinPoint.proceed()).thenReturn("ok");

        Object result = aspect.invoke(joinPoint);

        assertEquals("ok", result);
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void invoke_enabled_exception_incrementsCallCount_andPropagates() throws Throwable {
        aspect.setEnabled(true);
        when(joinPoint.proceed()).thenThrow(new IllegalStateException("boom"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> aspect.invoke(joinPoint));
        assertEquals("boom", ex.getMessage());
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void invoke_disabled_doesNotIncrementCount_andReturnsValue() throws Throwable {
        aspect.setEnabled(false);
        when(joinPoint.proceed()).thenReturn(42);

        Object result = aspect.invoke(joinPoint);

        assertEquals(42, result);
        assertEquals(0, aspect.getCallCount());
        assertEquals(0L, aspect.getCallTime());
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void reset_clearsCounters() throws Throwable {
        aspect.setEnabled(true);
        when(joinPoint.proceed()).thenReturn("x");

        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);
        assertEquals(2, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);

        aspect.reset();
        assertEquals(0, aspect.getCallCount());
        assertEquals(0L, aspect.getCallTime());
    }
}