package dk.logb.javase.labs.sayings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
    Fun with Danish sayings (da: ordsprog og talemåder)
    Source: https://sprogteknologi.dk/dataset/1000-talemader-evalueringsdatasaet/resource/90df32e7-5016-4180-8871-9e104a5e5bfe
 */
public class SayingApp {



    public static void main(String[] args) {
        Path filePath = Path.of("./src/main/java/dk/logb/javase/labs/sayings/talemaader_leverance_1.csv");

        //build list of sayings (ordsprog/talemaader)
        List<Saying> list = new ArrayList<>();
        try (var lines = Files.lines(filePath).skip(1)) {
            lines.forEach(line ->{
                    list.add(getSayingFromLine(line));
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        //print first saying
        System.out.println(list.getFirst());

        //longest saying
        Optional<Saying> longestSaying = list.stream()
                .max((s1, s2) -> Integer.compare(s1.saying().length(), s2.saying().length()));
        System.out.println("Longest saying: " + longestSaying);

        //number of sayings containing 'rige'
        System.out.println(list.stream().filter(s -> s.saying().contains("rige")).count());

        //print the ones containing 'rige'
        System.out.println(list.stream().filter(s -> s.saying().contains("rige")).collect(Collectors.toList()));

        //print as xml
        System.out.println(getAsXml(list));

        //print as json
        System.out.println(getAsJson(list));

        System.out.println("gladhus: --->" + fuzzyFindSayings("gladhus", list));
        System.out.println("vilde: --->" + fuzzyFindSayings("vilde", list));
        long start = System.currentTimeMillis();
        System.out.println("este: --->" + fuzzyFindSayings("este", list));
        long end = System.currentTimeMillis();
        System.out.println("Tid: " + (end-start));
    }

    /**
     *
     * @param searchString similar strings to search for
     * @param list
     * @return
     */
    private static List<Saying> fuzzyFindSayings(String searchString, List<Saying> list) {
        final var MIN_LENGTH = 4;
        final var MIN_LEV_DISTANCE = 2;

        if (searchString.length()<MIN_LENGTH) {
            throw new IllegalArgumentException("searchString must be longer than 3 characters");
        }
        ArrayList<Saying> result = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            Saying saying = list.get(i);
            String s = saying.toString();
            List<String> words = Utilities.tokenize(s);
            for (int j = 0; j < words.size(); j++) {
                if (words.get(j).length() < MIN_LENGTH) {
                    continue;
                }
                //if levenshteinDistance is small then add the saying to the result
                if (Utilities.levenshteinDistance(searchString, words.get(j)) < MIN_LEV_DISTANCE) {
                    result.add(list.get(i));
                    break;  //stop processing this saying
                }
            }
        }
        return result;
    }

    private static String getAsXml(List<Saying> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<sayings>\n");
        list.stream().forEach(s -> sb.append(s.toXml()));
        sb.append("</sayings>\n");
        return sb.toString();
    }

    private static String getAsJson(List<Saying> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toJson());
            if (i<list.size()-1) { //omit last comma
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static Saying getSayingFromLine(String line) {
        String[] parts = line.split("\t");
        return new Saying(Integer.parseInt(parts[0]), parts[1], parts[2]);
    }
}

record Saying(int id, String saying, String meaning) {
    /**
     * Transforms this instance into an XML representation.
     *
     * @return the XML string representation of this Saying instance
     */
    public String toXml() {
        return "\t<saying>\n" +
                "\t\t<id>" + id + "</id>\n" +
                "\t\t<saying>" + saying + "</saying>\n" +
                "\t\t<meaning>" + meaning + "</meaning>\n" +
                "\t</saying>\n";
    }

    /**
     * Transforms this instance into a JSON representation.
     *
     * @return the JSON string representation of this Saying instance
     */
    public String toJson() {
        return "{" +
                "\"id\": " + id + ", " +
                "\"saying\": \"" + saying + "\", " +
                "\"meaning\": \"" + meaning + "\"" +
                "}";
    }
}
