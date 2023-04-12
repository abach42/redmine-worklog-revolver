package com.abach42.redmineworklogrevolver.ProcedureChain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;

@ExtendWith(MockitoExtension.class)
public class AbstractProcedureHandlerTest {

    @Mock
    public ContextInterface context;

    @InjectMocks
    public AbstractProcedureHandler firstHandler = Mockito.mock(
        AbstractProcedureHandler.class, Mockito.CALLS_REAL_METHODS);

    @InjectMocks
    public AbstractProcedureHandler secondHandler = Mockito.mock(
        AbstractProcedureHandler.class, Mockito.CALLS_REAL_METHODS);
    
    @Test
    @DisplayName("Sets next handler")
    public void testSetNext() {
        firstHandler.setNext(secondHandler);

        assertThat(firstHandler.next).isEqualTo(secondHandler);
    }
    
    @Test
    @DisplayName("last handler's next should be null")
    public void testSetNextLastNull() {
        firstHandler.setNext(secondHandler);

        assertThat(secondHandler.next).isNull();
    }

 
    @Test
    @DisplayName("Should handle next link")
    public void testHandleNext() {
        doThrow(new IllegalArgumentException()).when(secondHandler).handle();
        firstHandler.setNext(secondHandler);

        assertThrows(IllegalArgumentException.class, () -> firstHandler.handleNext());
    }

    @Test
    @DisplayName("Should not handle next if null")
    public void testHandleNextNotIfNull() {
        doThrow(new IllegalArgumentException()).when(secondHandler).handle();
        firstHandler.setNext(secondHandler);
        firstHandler.setNext(secondHandler);
        firstHandler.next = null;

        assertDoesNotThrow(() -> firstHandler.handleNext());
    }

    @Test
    @DisplayName("Should make an endless chain")
    public void testInitializeEndlessChain() {

        AbstractProcedureHandler subject = AbstractProcedureHandler.initializeEndlessChain(
            firstHandler,
            secondHandler);
        
        assertThat(subject.next.next).isEqualTo(firstHandler);
    }

    @Test
    @DisplayName("Returns first link")
    public void testInitializeEndlessChainReturnsFirstLink() {

        AbstractProcedureHandler subject = AbstractProcedureHandler.initializeEndlessChain(
            firstHandler,
            secondHandler);
        
        assertThat(subject).isEqualTo(firstHandler);
    }
    
}
