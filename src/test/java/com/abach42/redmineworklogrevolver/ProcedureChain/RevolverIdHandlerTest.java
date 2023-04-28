package com.abach42.redmineworklogrevolver.ProcedureChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdTargetInterface;
import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Entity.Worklog;

@ExtendWith(MockitoExtension.class)
public class RevolverIdHandlerTest {

    @Mock
    protected ContextInterface context;

    @InjectMocks
    protected RevolverIdHandler subject = Mockito.mock(
        RevolverIdHandler.class, Mockito.CALLS_REAL_METHODS);

    @Test
    protected void testFetchRevolverIdsConcurrent() throws InterruptedException {
        // Create a test WorklogList
        WorklogList testList = new WorklogList();
        Worklog worklog1 = new Worklog(1, LocalDate.ofEpochDay(1), 1.0);
        Worklog worklog2 = new Worklog(2, LocalDate.ofEpochDay(1), 1.0);
        Worklog worklog3 = new Worklog(3, LocalDate.ofEpochDay(1), 1.0);
        testList.add(worklog1);
        testList.add(worklog2);
        testList.add(worklog3);

    //TODO: think about mocking
        // Create a mock RevolverIdTargetInterface
        RevolverIdTargetInterface mockTarget = mock(RevolverIdTargetInterface.class);
        doReturn(mockTarget).when(subject).setupAdapter();
        when(mockTarget.singleRevolverId(any(ApiDemand.class))).thenReturn("revolver1");
        

        // Call the fetchRevolverIdsConcurrent method
        subject.fetchRevolverIdsConcurrent(testList);

        // Verify that the revolver IDs were added to each Worklog object
        assertEquals("revolver1", worklog1.getRevolverIdentifier());
        assertEquals("revolver1", worklog2.getRevolverIdentifier());
        assertEquals("revolver1", worklog3.getRevolverIdentifier());

        // Verify that the executor was stopped after all tasks completed
       // assertTrue(executor.isTerminated());
    }

}
