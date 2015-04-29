package app.contacts.com.contacts.models;

public class Message {

    private String name;
    private String phoneNumber;
    private String message;
    private String date;
    private int type;

    public Message(){}

    public Message(String name, String phoneNumber, String message, String date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.date = date;
    }

    public Message(String name, String phoneNumber, String message, String date, int type) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.date = date;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
