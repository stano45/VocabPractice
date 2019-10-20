package com.stanley.vocabpractice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class WordGroup implements Serializable {
    String groupName = "";
    List<Word> wordList = new ArrayList<>();
}
