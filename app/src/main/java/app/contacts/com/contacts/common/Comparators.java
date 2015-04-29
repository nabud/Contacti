package app.contacts.com.contacts.common;

import java.util.Comparator;

public class Comparators {

    public static class GenericComparator implements Comparator<Object> {

        private String getter;
        private int order;

        public GenericComparator(String field, int order) {
            this.getter = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
            this.order = order;
        }

        @Override
        public int compare(Object o1, Object o2) {

            try {

                if (o1 != null && o2 != null) {
                    o1 = ((String) o1.getClass().getMethod(getter, new Class[0]).invoke(o1, new Object[0])).toLowerCase();
                    o2 = ((String) o2.getClass().getMethod(getter, new Class[0]).invoke(o2, new Object[0])).toLowerCase();
                }

            } catch (Exception e) {
                // If this exception occurs, then it is usually a fault of the developer.
                throw new RuntimeException("Cannot compare " + o1 + " with " + o2 + " on " + getter, e);
            }

            return (order == Constants.ASCENDING) ? (o1 == null) ? -1 : ((o2 == null) ? 1 : ((String) o1).compareTo((String) o2))
                                                  : (o1 == null) ? -1 : ((o2 == null) ? 1 : ((String) o2).compareTo((String) o1));
        }
    }

}
