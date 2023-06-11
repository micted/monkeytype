package model;

import java.util.List;

public class Language {
    private String name;
    private List<String> dictionary;

    public Language(String name, List<String> dictionary) {
        this.name = name;
        this.dictionary = dictionary;
    }

    public String getName() {
        return name;
    }

    public List<String> getDictionary() {
        return dictionary;
    }
    
    @Override
    public String toString() {
        return getName(); // Return the name of the language as the string representation
    }
}
