package app.contacts.com.contacts.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import app.contacts.com.contacts.common.Comparators;
import app.contacts.com.contacts.models.Contact;

public class Duplicates {

    /**
     * Find duplicates by one field
     * @param contacts
     * @param fieldName
     * @return
     */
    public static Map<String, List<Contact>> findDuplicatesByOneField(List<Contact> contacts, String fieldName) {
        return findDuplicatesUsingMap(contacts, fieldName);
    }

    /**
     * Find duplicates by multiple fields
     * @param contacts
     * @param fieldNames
     * @return
     */
    public static List<Contact> findDuplicatesByMultipleFields(List<Contact> contacts, String... fieldNames) {

        List<Contact> duplicates = new ArrayList<>();
        for (String fieldName: fieldNames)
            duplicates.addAll(findDuplicates(contacts, fieldName));
        return duplicates;
    }

    private static List<Contact> findDuplicates(List<Contact> contacts, String fieldName) {

        List<Contact> duplicates = new ArrayList<>();
        Set<Contact> contactSet = new TreeSet<>(new Comparators.GenericComparator(fieldName, Constants.ASCENDING));
        for(Contact contact: contacts) {
            if(!contactSet.add(contact))
                duplicates.add(contact);
        }
        return duplicates;
    }

    private static Map<String, List<Contact>> findDuplicatesUsingMap(List<Contact> contacts, String fieldName) {

        Map<String, List<Contact>> map = new ConcurrentHashMap<>();
        for(Contact contact : contacts){
            if(map.containsKey(contact.getFullName()))
                map.get(contact.getFullName()).add(contact);

            else {
                map.put(contact.getFullName(), new ArrayList<Contact>());
                map.get(contact.getFullName()).add(contact);
            }
        }

        for(String fullName :map.keySet())
            if(map.get(fullName).size() <= 1)
                map.remove(fullName);
        return map;
    }
}
