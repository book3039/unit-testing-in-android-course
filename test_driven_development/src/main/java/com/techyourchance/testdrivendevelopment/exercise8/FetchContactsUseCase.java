package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    public interface Listener {
        void onContactFetched(List<Contact> contacts);
        void onFetchContactFailed();
        void onFetchFailedWithNetworkError();
    }
    private final GetContactsHttpEndpoint getContactsHttpEndPoint;
    private final List<Listener> listeners = new ArrayList<>();

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndPoint) {
        this.getContactsHttpEndPoint = getContactsHttpEndPoint;
    }

    public void fetchContactAndNotify(String filterTerm) {
        getContactsHttpEndPoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> schemas) {
                for (Listener listener : listeners) {
                    listener.onContactFetched(contactsFromSchema(schemas));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                if (failReason == GetContactsHttpEndpoint.FailReason.NETWORK_ERROR) {
                    for (Listener listener : listeners) {
                        listener.onFetchFailedWithNetworkError();
                    }
                } else {
                    for (Listener listener : listeners) {
                        listener.onFetchContactFailed();
                    }
                }
            }
        });
    }

    private List<Contact> contactsFromSchema(List<ContactSchema> schemas) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactSchema schema : schemas) {
            contacts.add(new Contact(schema.getId(), schema.getFullName(), schema.getImageUrl()));
        }

        return contacts;
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }
}
