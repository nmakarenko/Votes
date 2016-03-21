package org.bissoft.yean.votes.util;

import java.util.Comparator;

public class Person {
    public String name;
    public int sex;
    public int poll;
    public String nameFrak;

    public Person(String name, int sex, int poll, String nameFrak){
        this.name = name;
        this.sex = sex;
        this.poll = poll;
        this.nameFrak = nameFrak;
    }

    public static Comparator<Person> PersoneComparator = new Comparator<Person>() {

        public int compare(Person s1, Person s2) {
            String PersoneName1 = s1.name.toUpperCase();
            String PersoneName2 = s2.name.toUpperCase();

            //ascending order
            return PersoneName1.compareTo(PersoneName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}

