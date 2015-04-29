package app.contacts.com.contacts.common;

import java.util.Collections;
import java.util.List;

import app.contacts.com.contacts.models.Contact;

public class Sort {

    public static void sort(List<Contact> contacts, String field, int order) {
        Collections.sort(contacts, new Comparators.GenericComparator(field, order));
    }
}