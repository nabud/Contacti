package app.contacts.com.contacts.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StringUtil {

    /**
     * Check is null
     * @param string
     * @return
     */
    public static boolean isNull(String string) {
        return string == null ? true : false;
    }

    /**
     * Check is null or empty
     * @param field
     * @return
     */
    public static boolean isNullOrEmpty(String field) {
        return (isNull(field) || field.trim().length() == 0) ? true : false;
    }

    /**
     * Check is null or empty
     * @param fields
     * @return
     */
    public static boolean isNullOrEmpty(String... fields) {
        boolean valid = true;
        for (String field: fields) {
            valid = valid && isNullOrEmpty(field);
        }
        return valid;
    }

    /**
     * Remove all characters from work
     * @param word
     * @return
     */
    public static String removeCharactersFromPhoneNumber(String word) {
        String newWord = word;
        if (word.startsWith("1"))
            newWord = word.substring(1);
        else if (word.startsWith("+1"))
            newWord = word.substring(2);
        return newWord.replaceAll("[^\\d]", "").trim();
    }

    /**
     * Format phone number
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {
        return (phoneNumber.length() == 10) ? phoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)$2-$3") : phoneNumber;
    }

    /**
     * Clean and format phone number
     * @param phoneNumber
     * @return
     */
    public static String cleanAndFormatPhoneNumber(String phoneNumber) {
        String newPhoneNumber = removeCharactersFromPhoneNumber(phoneNumber);
        return (newPhoneNumber.length() == 10) ? newPhoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)$2-$3") : phoneNumber;
    }

    public static String cleanAndFormatPhoneNumber(String phoneNumber, String replacement) {
        return (!isNullOrEmpty(phoneNumber) ? cleanAndFormatPhoneNumber(phoneNumber) : replacement);
    }

    /**
     * Sub-string a word from a sentence based on a character
     * @param sentence
     * @param character
     * @return
     */
    public static String getWordFromSentence(String sentence, char character) {
        if (!StringUtil.isNullOrEmpty(sentence) && sentence.contains(String.valueOf(character)))
            sentence = sentence.substring(0, sentence.indexOf(character));
        return sentence;
    }

    /**
     * Split a string by a specified character
     * @param sentence
     * @param delimiter
     * @return
     */
    public static String[] splitString(String sentence, String delimiter) {
        if (!StringUtil.isNullOrEmpty(sentence))
            return sentence.split(delimiter);
        return new String[]{};
    }

    /**
     * Split a string by the first occurance of a specified character
     * @param sentence
     * @param delimiter
     * @return
     */
    public static String[] splitStringFirstOccurance(String sentence, String delimiter) {
        if (!StringUtil.isNullOrEmpty(sentence))
            return sentence.split(delimiter, 2);
        return new String[]{};
    }

    /**
     * Split a string by a specified character
     * @param sentence
     * @param delimiter
     * @return
     */
    public static List<String> splitStringToList(String sentence, String delimiter) {
        if (!StringUtil.isNullOrEmpty(sentence))
            return Arrays.asList(sentence.split(delimiter));
        return new ArrayList<>();
    }

    /**
     * Get first word from sentence
     * @param sentence
     * @return
     */
    public static String getFirstWordFromSentence(String sentence) {
        return sentence.substring(0, sentence.indexOf(" "));
    }

    /**
     * Generate id
     * @param someId
     * @return
     */
    public static String generateId(String someId) {
        return someId.concat(new Date().toString());
    }

    /**
     * Construct string
     * @param fields
     * @return
     */
    public static String constructString(String... fields) {

        String result="";
        for (String field: fields)
            result.concat(field);
        return result;
    }
    /**
     * Construct string with delimiter
     * @param fields
     * @return
     */
    public static String constructStringWithDelimiter(String delimiter, String... fields) {

        StringBuilder builder = new StringBuilder();
        for (int index=0; index < fields.length; index++)
            if (index < fields.length)
                builder.append(fields[index]).append(delimiter);
        return builder.toString();
    }

    /**
     * Abbreviate String
     * @param string
     * @return
     */
    public static String abbreviate(String string){
        return (string.length() > 17) ? string.substring(0, Math.min(string.length(), 17)) + "..." : string;
    }

    public static String returnEmptyIfNull(String token) {
        return (!isNullOrEmpty(token)) ? token : "";
    }

    public static String returnEmptyIfNull(String token, String returned) {
        return (!isNullOrEmpty(token)) ? token : returned;
    }
}
