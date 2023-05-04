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
        WorklogList testList = new WorklogList();
        Worklog worklog1 = new Worklog(1, LocalDate.ofEpochDay(1), 1.0);
        Worklog worklog2 = new Worklog(2, LocalDate.ofEpochDay(1), 1.0);
        Worklog worklog3 = new Worklog(3, LocalDate.ofEpochDay(1), 1.0);
        testList.add(worklog1);
        testList.add(worklog2);
        testList.add(worklog3);
    
        doAnswer(invocation -> {
            Worklog worklog = invocation.getArgument(0);
            worklog.setRevolverIdentifier("foobar");
            return worklog;
        })
        .when(subject).addRevolverIdToWorlog(any(Worklog.class), any(RevolverIdTargetInterface.class));
        
        // Call the fetchRevolverIdsConcurrent method
        subject.fetchRevolverIdsConcurrent(testList);

        // Verify that the revolver IDs were added to each Worklog object
        assertEquals("foobar", worklog1.getRevolverIdentifier());
        assertEquals("foobar", worklog2.getRevolverIdentifier());
        assertEquals("foobar", worklog3.getRevolverIdentifier());

        // Verify that the executor was stopped after all tasks completed
       // assertTrue(executor.isTerminated());
    }

}
