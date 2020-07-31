package com.conferenceManagement.util;

import java.text.Normalizer;

public class UnicodeStringComparator {

    public static boolean equals(String s1, String s2){
        return removeDiacriticalMarks(s1).equals(removeDiacriticalMarks(s2));
    }

    public static boolean contains(String container, String regex){
        return removeDiacriticalMarks(container).contains(removeDiacriticalMarks(regex));
    }

    //reference from https://stackoverflow.com/questions/2397804/java-string-searching-ignoring-accents
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
