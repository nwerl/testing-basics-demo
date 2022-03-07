package com.demo.testing.mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ActionHandlerTest {
    ActionHandler actionHandler;

    @Mock
    Service service;
    @Mock
    Response response;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        actionHandler = new ActionHandler(service);
    }

    @Test
    public void getValue_should_return_valid_string_if_doRequest_succeed() {
        // Given
        when(response.isSuccessful()).thenReturn(true);
        when(response.getData()).thenReturn("Success");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.reply(response);
            return null;
        }).when(service).getResponse(anyString(), isA(Callback.class));

        // When
        actionHandler.doRequest("anyQuery");

        // Then
        String value = actionHandler.getValue();
        assertThat(value, is("Success"));
    }

    @Captor
    ArgumentCaptor<Callback> mCaptor;

    @Test
    public void getValue_should_return_valid_string_if_doRequest_succeed_V2() {
        // Given
        when(response.isSuccessful()).thenReturn(true);
        when(response.getData()).thenReturn("Success");

        //ArgumentCaptor 사용해 볼것
        doNothing().when(service).getResponse(anyString(), mCaptor.capture() /*TODO*/);

        // When
        actionHandler.doRequest("anyQuery");

        // Then
        mCaptor.getValue().reply(response);
        String value = actionHandler.getValue();
        assertThat(value, is("Success"));
    }

    @Test
    public void getValue_should_return_valid_string_if_doRequest_succeed_V3() {
        // Given
        when(response.isSuccessful()).thenReturn(true);
        when(response.getData()).thenReturn("Success");

        // When
        actionHandler.doRequest("anyQuery");

        // Then
        verify(service).getResponse(anyString(), mCaptor.capture());
        mCaptor.getValue().reply(response);

        String value = actionHandler.getValue();
        assertThat(value, is("Success"));
    }

    @Test
    public void getValue_should_return_valid_string_if_doRequest_failed() {
        // Given
        when(response.isSuccessful()).thenReturn(false);

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.reply(response);
            return null;
        }).when(service).getResponse(anyString(), isA(Callback.class));
        // When
        actionHandler.doRequest("anyQuery");

        // Then
        String value = actionHandler.getValue();
        assertThat(value, is(nullValue()));
    }


    @Test
    public void getValue_should_return_null_if_doRequest_failed() {
        // Given

        // When

        // Then

    }
}