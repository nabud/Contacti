package app.contacts.com.contacts.models;

import java.util.List;

public class DuplicateContacts {

    private String contactName;
    private List<Contact> list;

    public DuplicateContacts(){}

    public DuplicateContacts(String contactName, List<Contact> contacts) {
        this.contactName = contactName;
        this.list = contacts;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<Contact> getList() {
        return list;
    }

    public void setList(List<Contact> list) {
        this.list = list;
    }


}
