package com.techyourchance.testdrivendevelopment.exercise8;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {

    // region constants
    public static final String FILTER_TERM = "John";
    public static final String ID = "id";
    public static final String FULL_NAME = "fullName";
    public static final String FULL_PHONE_NUMBER = "fullPhoneNumber";
    public static final String IMAGE_URL = "imageUrl";
    public static final double AGE = 32.0;
    // endregion constants

    // region helper fields
    GetContactsHttpEndPointTd getContactsHttpEndPointTd;
    @Mock
    FetchContactsUseCase.Listener listenerMock1;
    @Mock
    FetchContactsUseCase.Listener listenerMock2;
    @Captor
    ArgumentCaptor<List<Contact>> acListContact;
    // endregion helper fields

    FetchContactsUseCase SUT;

    @Before
    public void setUp() throws Exception {
        getContactsHttpEndPointTd = new GetContactsHttpEndPointTd();
        SUT = new FetchContactsUseCase(getContactsHttpEndPointTd);
    }

    @Test
    public void fetchContact_correctFilterTermPassedToEndPoint() throws Exception {
        // Arrange
        // Act
        SUT.fetchContactAndNotify(FILTER_TERM);
        // Assert
        assertEquals(getContactsHttpEndPointTd.invocationCount, 1);
        assertEquals(getContactsHttpEndPointTd.filterTerm, FILTER_TERM);
    }

    @Test
    public void fetchContact_success_observersNotifiedWithCorrectData() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactFetched(acListContact.capture());
        verify(listenerMock2).onContactFetched(acListContact.capture());

        List<List<Contact>> captures = acListContact.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);

        assertEquals(capture1, getContacts());
        assertEquals(capture2, getContacts());
    }

    @Test
    public void fetchContact_success_unsubscribedObserversNotNotified() throws Exception {
        // Arrange
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.unregisterListener(listenerMock2);
        SUT.fetchContactAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactFetched(any(List.class));
        verifyNoMoreInteractions(listenerMock2);
    }

    @Test
    public void fetchContact_generalError_observersNotifiedOfFailure() throws Exception {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onFetchContactFailed();
        verify(listenerMock2).onFetchContactFailed();
    }

    @Test
    public void fetchContact_networkError_observersNotifiedOfNetworkError() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(listenerMock1);
        SUT.registerListener(listenerMock2);
        SUT.fetchContactAndNotify(FILTER_TERM);
        // Assert
        verify(listenerMock1).onFetchFailedWithNetworkError();
        verify(listenerMock2).onFetchFailedWithNetworkError();
    }

    // region helper methods

    private List<ContactSchema> getContactSchemas() {
        List<ContactSchema> contactSchemas = new ArrayList<>();
        contactSchemas.add(new ContactSchema(
                ID,
                FULL_NAME,
                FULL_PHONE_NUMBER,
                IMAGE_URL,
                AGE));

        return contactSchemas;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(
                ID,
                FULL_NAME,
                IMAGE_URL
        ));

        return contacts;
    }

    private void generalError() {
        getContactsHttpEndPointTd.generalError = true;
    }

    private void networkError() {
        getContactsHttpEndPointTd.networkError = true;
    }

    // endregion helper methods
    // region helper classes
    private class GetContactsHttpEndPointTd implements GetContactsHttpEndpoint {
        private int invocationCount;
        private String filterTerm;

        private boolean generalError;
        public boolean networkError;

        @Override
        public void getContacts(String filterTerm, Callback callback) {
            invocationCount++;
            this.filterTerm = filterTerm;

            if (generalError) {
                callback.onGetContactsFailed(FailReason.GENERAL_ERROR);
            } else if (networkError) {
                callback.onGetContactsFailed(FailReason.NETWORK_ERROR);
            } else {
                callback.onGetContactsSucceeded(getContactSchemas());
            }
        }
    }
    // endregion helper classes
}