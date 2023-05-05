package com.kurenkievtimur;

public enum AgeGroup {
    ONE(1, "5-6", "Kindergarten"),
    TWO(2, "6-7", "First Grade"),
    THREE(3, "7-8", "Second Grade"),
    FOUR(4, "8-9", "Third Grade"),
    FIVE(5, "9-10", "Fourth Grade"),
    SIX(6, "10-11", "Fifth Grade"),
    SEVEN(7, "11-12", "Sixth Grade"),
    EIGHT(8, "12-13", "Seventh Grade"),
    NINE(9, "13-14", "Eighth Grade"),
    TEN(10, "14-15", "Ninth Grade"),
    ELEVEN(11, "15-16", "Ninth Grade"),
    TWELVE(12, "16-17", "Eleventh Grade"),
    THIRTEEN(13, "17-18", "Twelfth Grade"),
    FOURTEEN(14, "18-22", "College student");

    private final int score;

    private final String age;

    private final String grade;

    AgeGroup(int score, String age, String grade) {
        this.score = score;
        this.age = age;
        this.grade = grade;
    }

    public int getScore() {
        return score;
    }

    public String getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    public static AgeGroup getGroupByScore(double score) {
        int scoreCeilInt = (int) Math.round(score);
        for (AgeGroup group : AgeGroup.values()) {
            if (group.getScore() == scoreCeilInt) {
                return group;
            }
        }

        throw new IllegalStateException("Unexpected value: " + score);
    }
}
