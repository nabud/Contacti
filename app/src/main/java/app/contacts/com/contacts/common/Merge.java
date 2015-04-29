package app.contacts.com.contacts.common;

import java.util.ArrayList;
import java.util.List;

import app.contacts.com.contacts.models.Contact;
import app.contacts.com.contacts.models.DuplicateContacts;

public class Merge {

    /**
     * Merge
     * @param contacts
     * @param duplicates
     * @return
     */
    public static List<Contact> merge(List<Contact> contacts, List<DuplicateContacts> duplicates) {

        for (DuplicateContacts duplicateContact: duplicates) {

            for (Contact duplicate: duplicateContact.getList()) {
                for (int index = 0; index < contacts.size(); index++) {

                    Contact contact = contacts.get(index);
                    if (duplicate.getContactId().equals(contact.getContactId())) {
                        contact.getPhoneNumbers().add(contact.getPrimaryPhoneNumber());
                        contact.getEmailAddresses().add(contact.getPrimaryEmailAddress());

                        if (contact.getPhoneNumbers() == null)
                            contact.setPhoneNumbers(new ArrayList());

                        else if (duplicate.getPhoneNumbers() != null && !duplicate.getPhoneNumbers().isEmpty()) {

                            for (String phoneNumber : contact.getPhoneNumbers()) {
                                if (!duplicate.getPhoneNumbers().contains(phoneNumber))
                                    contact.getPhoneNumbers().add(phoneNumber);
                            }
                        }

                        if (contact.getEmailAddresses() == null)
                            contact.setEmailAddresses(new ArrayList());

                        else if (duplicate.getEmailAddresses() != null && !duplicate.getEmailAddresses().isEmpty()) {

                            for (String emailAddress : contact.getEmailAddresses()) {
                                if (!duplicate.getEmailAddresses().contains(emailAddress))
                                    contact.getEmailAddresses().add(emailAddress);
                            }
                        }
                        contacts.remove(duplicate);
                    }
                }
                System.out.println("\n----------------------------------------------");
                System.out.println(duplicate.getFullName());
                System.out.println(duplicate.getPrimaryPhoneNumber());


            }
        }

//        for (Contact contact: contacts) {
//            System.out.println("\n----------------------------------------------");
//            System.out.println(contact.getFullName());
//
//            System.out.println("primary phoneNumber: " + contact.getPrimaryPhoneNumber());
//            if (contact.getPhoneNumbers() != null)
//                for (String number: contact.getPhoneNumbers())
//                    System.out.println(number);
//
//            System.out.println("primary emailAddress: " + contact.getPrimaryEmailAddress());
//            if (contact.getEmailAddresses() != null)
//                for (String email: contact.getEmailAddresses())
//                    System.out.println(email);
//        }

        return contacts;
    }
}
