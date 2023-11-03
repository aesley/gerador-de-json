package org.json;

import java.util.*;
import java.io.*;

public class JSONLGenerator {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Create a LinkedHashMap to maintain the order of insertion
        LinkedHashMap<String, Object> contextMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> importsMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> sourceMap = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, Object>> intentsList = new ArrayList<>();

        // Read imports
        ArrayList<String> importsList = new ArrayList<>();
        System.out.println("Enter Java imports (type 'end' to finish):");
        while(scanner.hasNext()) {
            String importEntry = scanner.nextLine();
            if("end".equalsIgnoreCase(importEntry)) {
                break;
            }
            importsList.add(importEntry);
        }
        importsMap.put("java", importsList);

        // Read source
        System.out.println("Enter source URL:");
        String sourceUrl = scanner.nextLine();
        System.out.println("Enter source language:");
        String sourceLanguage = scanner.nextLine();
        System.out.println("Enter source HTML:");
        String sourceHtml = scanner.nextLine();
        sourceMap.put("url", sourceUrl);
        sourceMap.put("language", sourceLanguage);
        sourceMap.put("html", sourceHtml);

        // Read intents
        while(true) {
            LinkedHashMap<String, Object> intentMap = new LinkedHashMap<>();
            System.out.println("Enter intent (type 'end' to finish):");
            String intent = scanner.nextLine();
            if("end".equalsIgnoreCase(intent)) {
                break;
            }
            intentMap.put("intent", intent);

            System.out.println("Enter intent-generated:");
            String intentGenerated = scanner.nextLine();
            intentMap.put("intent-generated", intentGenerated);

            LinkedHashMap<String, Object> codeMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> seleniumMap = new LinkedHashMap<>();
            System.out.println("Enter Java code for Selenium (type 'end' to finish):");
            ArrayList<String> seleniumCodeList = new ArrayList<>();
            while(scanner.hasNext()) {
                String codeEntry = scanner.nextLine();
                if("end".equalsIgnoreCase(codeEntry)) {
                    break;
                }
                seleniumCodeList.add(codeEntry);
            }
            seleniumMap.put("java", seleniumCodeList);
            codeMap.put("selenium", seleniumMap);

            intentMap.put("code", codeMap);

            LinkedHashMap<String, Object> entitiesMap = new LinkedHashMap<>();
            System.out.println("Enter target (type 'end' to finish):");
            ArrayList<String> targetList = new ArrayList<>();
            while(scanner.hasNext()) {
                String targetEntry = scanner.nextLine();
                if("end".equalsIgnoreCase(targetEntry)) {
                    break;
                }
                targetList.add(targetEntry);
            }
            entitiesMap.put("target", targetList);

            ArrayList<String> relatedList = new ArrayList<>(); // Assuming 'related' is always an empty list
            entitiesMap.put("related", relatedList);

            intentMap.put("entities", entitiesMap);

            intentMap.put("values", new LinkedHashMap<>()); // Assuming 'values' is always an empty map

            intentsList.add(intentMap);
        }

        // Assemble the JSON structure
        contextMap.put("imports", importsMap);
        contextMap.put("source", sourceMap);
        contextMap.put("intents", intentsList);

        // Convert the map to JSON String
        String jsonString = toJsonString(contextMap);

        // Print the JSON String in one single line
        String singleLineJson = jsonString.replaceAll("\n", "").replaceAll("\\s+", " ");
        System.out.println(singleLineJson);

        // Write the single line JSON to a file
        writeToFile(singleLineJson, "output.txt");

        // Close the scanner
        scanner.close();
    }

    // Method to convert a map to JSON String
    private static String toJsonString(Map<String, Object> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        map.forEach((key, value) -> {
            jsonBuilder.append("\t\"").append(key).append("\": ");
            if (value instanceof Map) {
                jsonBuilder.append(toJsonString((Map<String, Object>) value));
            } else if (value instanceof List) {
                jsonBuilder.append("[\n");
                ((List<?>) value).forEach(item -> {
                    jsonBuilder.append("\t\t\"").append(item).append("\",\n");
                });
                if(!((List<?>) value).isEmpty()) {
                    jsonBuilder.setLength(jsonBuilder.length() - 2); // Remove last comma and newline
                }
                jsonBuilder.append("\n\t],\n");
            } else {
                jsonBuilder.append("\"").append(value).append("\",\n");
            }
        });
        if(!map.isEmpty()) {
            jsonBuilder.setLength(jsonBuilder.length() - 2); // Remove last comma and newline
        }
        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    // Method to write String data to file
    private static void writeToFile(String data, String fileName) {
        // Ajuste o caminho para um local seguro, como o diretório temporário do sistema
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        File outputFile = new File(tempDirectoryPath, fileName);

        try (PrintWriter out = new PrintWriter(outputFile)) {
            out.println(data);
            System.out.println("JSON saved to " + outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}

