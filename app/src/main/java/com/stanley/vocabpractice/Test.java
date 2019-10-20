package com.stanley.vocabpractice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Test implements Serializable {
    int maxPoints;
    int numQuestions;
    int pointsCount;
    List<Integer> points = new ArrayList<>();
    WordGroup wordGroup;
    List<Word> wordsUsed = new ArrayList<>();
    List<Word> wordsInput = new ArrayList<>();
}
