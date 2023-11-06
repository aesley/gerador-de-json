import java.io.*;
import java.util.*;
//não funciona
public class JSONLGenerator {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LinkedHashMap<String, Object> context = new LinkedHashMap<>();
        LinkedHashMap<String, Object> imports = new LinkedHashMap<>();
        LinkedHashMap<String, Object> source = new LinkedHashMap<>();
        ArrayList<LinkedHashMap<String, Object>> intentsList = new ArrayList<>();

        // Read Java imports
        ArrayList<String> javaImports = new ArrayList<>();
        System.out.println("Enter Java imports (type 'end' to finish):");
        while (true) {
            String javaImport = scanner.nextLine();
            if ("end".equalsIgnoreCase(javaImport)) {
                break;
            }
            javaImports.add(javaImport);
        }
        imports.put("java", javaImports);

        // Read source details
        System.out.println("Enter source URL:");
        source.put("url", scanner.nextLine());
        System.out.println("Enter source language:");
        source.put("language", scanner.nextLine());
        System.out.println("Enter source HTML:");
        source.put("html", scanner.nextLine());

        // Read intents
        while (true) {
            LinkedHashMap<String, Object> intent = new LinkedHashMap<>();
            System.out.println("Enter intent (type 'end' to finish):");
            String intentName = scanner.nextLine();
            if ("end".equalsIgnoreCase(intentName)) {
                break;
            }
            intent.put("intent", intentName);

            System.out.println("Enter intent-generated:");
            intent.put("intent-generated", scanner.nextLine());

            LinkedHashMap<String, Object> code = new LinkedHashMap<>();
            LinkedHashMap<String, Object> selenium = new LinkedHashMap<>();
            ArrayList<String> javaCode = new ArrayList<>();
            System.out.println("Enter Java code with Selenium (type 'end' to finish):");
            while (true) {
                String codeLine = scanner.nextLine();
                if ("end".equalsIgnoreCase(codeLine)) {
                    break;
                }
                javaCode.add(codeLine);
            }
            selenium.put("java", javaCode);
            code.put("selenium", selenium);
            intent.put("code", code);

            LinkedHashMap<String, Object> entities = new LinkedHashMap<>();
            ArrayList<String> targetEntities = new ArrayList<>();
            System.out.println("Enter target entities (type 'end' to finish):");
            while (true) {
                String targetEntity = scanner.nextLine();
                if ("end".equalsIgnoreCase(targetEntity)) {
                    break;
                }
                targetEntities.add(targetEntity);
            }
            entities.put("target", targetEntities);
            entities.put("related", new ArrayList<>()); // Related entities are assumed to be empty
            intent.put("entities", entities);
            intent.put("values", new LinkedHashMap<>()); // Values are assumed to be empty

            intentsList.add(intent);
        }

        context.put("imports", imports);
        context.put("source", source);
        context.put("intents", intentsList);

        // Convert the context map to JSON String
        String jsonString = toJsonString(context);

        // Print the JSON String in one single line
        String singleLineJson = jsonString.replaceAll("\n", "").replaceAll("\\s+", " ");
        System.out.println(singleLineJson);

        // Write the single line JSON to a file
        writeToFile(singleLineJson, "output.txt");

        // Close the scanner
        scanner.close();
    }

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
                if (!((List<?>) value).isEmpty()) {
                    jsonBuilder.setLength(jsonBuilder.length() - 2); // Remove last comma and newline
                }
                jsonBuilder.append("\n\t],\n");
            } else {
                jsonBuilder.append("\"").append(value).append("\",\n");
            }
        });
        if (!map.isEmpty()) {
            jsonBuilder.setLength(jsonBuilder.length() - 2); // Remove last comma and newline
        }
        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    private static void writeToFile(String data, String fileName) {
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
