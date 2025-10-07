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
    void setEnabled_setsEnabledToTrue() {
        aspect.setEnabled(true);
        assertTrue(aspect.isEnabled());
    }

    @Test
    void setEnabled_setsEnabledToFalse() {
        aspect.setEnabled(false);
        assertFalse(aspect.isEnabled());
    }

    @Test
    void isEnabled_returnsTrueByDefault() {
        assertTrue(aspect.isEnabled());
    }

    @Test
    void isEnabled_returnsFalseWhenDisabled() {
        aspect.setEnabled(false);
        assertFalse(aspect.isEnabled());
    }

    @Test
    void reset_resetsCallCountAndAccumulatedTime() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);

        assertTrue(aspect.getCallCount() > 0);

        aspect.reset();

        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void getCallCount_returnsZeroInitially() {
        assertEquals(0, aspect.getCallCount());
    }

    @Test
    void getCallCount_returnsCorrectCount() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.invoke(joinPoint);
        assertEquals(1, aspect.getCallCount());

        aspect.invoke(joinPoint);
        assertEquals(2, aspect.getCallCount());

        aspect.invoke(joinPoint);
        assertEquals(3, aspect.getCallCount());
    }

    @Test
    void getCallTime_returnsZeroWhenNoCallsMade() {
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void getCallTime_returnsAverageTime() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenAnswer(invocation -> {
            Thread.sleep(10);
            return "result";
        });

        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);

        long averageTime = aspect.getCallTime();
        assertTrue(averageTime >= 0);
    }

    @Test
    void invoke_whenEnabled_monitorsCall() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.setEnabled(true);
        Object result = aspect.invoke(joinPoint);

        assertEquals("result", result);
        verify(joinPoint).proceed();
        assertEquals(1, aspect.getCallCount());
    }

    @Test
    void invoke_whenEnabled_incrementsCallCount() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.setEnabled(true);
        aspect.invoke(joinPoint);

        assertEquals(1, aspect.getCallCount());
    }

    @Test
    void invoke_whenEnabled_accumulatesTime() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.setEnabled(true);
        aspect.invoke(joinPoint);

        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void invoke_whenDisabled_proceedsWithoutMonitoring() throws Throwable {
        when(joinPoint.proceed()).thenReturn("result");

        aspect.setEnabled(false);
        Object result = aspect.invoke(joinPoint);

        assertEquals("result", result);
        verify(joinPoint).proceed();
        verify(joinPoint, never()).toShortString();
        assertEquals(0, aspect.getCallCount());
    }

    @Test
    void invoke_whenEnabled_propagatesException() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenThrow(new RuntimeException("test exception"));

        aspect.setEnabled(true);

        assertThrows(RuntimeException.class, () -> aspect.invoke(joinPoint));
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void invoke_returnsJoinPointResult() throws Throwable {
        String expectedResult = "expected result";
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn(expectedResult);

        aspect.setEnabled(true);
        Object result = aspect.invoke(joinPoint);

        assertEquals(expectedResult, result);
    }

    @Test
    void invoke_whenDisabled_returnsJoinPointResult() throws Throwable {
        String expectedResult = "expected result";
        when(joinPoint.proceed()).thenReturn(expectedResult);

        aspect.setEnabled(false);
        Object result = aspect.invoke(joinPoint);

        assertEquals(expectedResult, result);
    }

    @Test
    void invoke_multipleCalls_accumulatesCorrectly() throws Throwable {
        when(joinPoint.toShortString()).thenReturn("test");
        when(joinPoint.proceed()).thenReturn("result");

        aspect.setEnabled(true);
        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);
        aspect.invoke(joinPoint);

        assertEquals(3, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
    }
}