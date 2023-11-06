package org.json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
//FUNCIONAL
public class JSONGeneratortxt {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> contextMap = new LinkedHashMap<>();

        // Imports section
        System.out.println("Insira os imports do Java (linha vazia encerra o campo):");
        List<String> imports = new ArrayList<>();
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            imports.add(line);
        }
        Map<String, List<String>> importsMap = new LinkedHashMap<>();
        importsMap.put("java", imports);
        contextMap.put("imports", importsMap);

        // Source section
        Map<String, String> sourceMap = new LinkedHashMap<>();
        System.out.println("Insira a URL:");
        sourceMap.put("url", scanner.nextLine());
        System.out.println("Insira a linguagem da pagina HTML:");
        sourceMap.put("language", scanner.nextLine());
        System.out.println("Insira o conteúdo HTML:");
        sourceMap.put("html", scanner.nextLine());
        contextMap.put("source", sourceMap);

        // Intents section
        List<Map<String, Object>> intentsList = new ArrayList<>();
        boolean addMoreIntents;
        do {
            Map<String, Object> intentMap = new LinkedHashMap<>();
            System.out.println("Insira o intent:");
            intentMap.put("intent", scanner.nextLine());
            System.out.println("Insira o intent-generated :");
            intentMap.put("intent-generated", scanner.nextLine());

            // Code section
            Map<String, List<String>> seleniumMap = new LinkedHashMap<>();
            System.out.println("Insira o codigo Java com Selenium (linha vazia encerra o campo):");
            List<String> javaCode = new ArrayList<>();
            while (!(line = scanner.nextLine()).isEmpty()) {
                javaCode.add(line);
            }
            seleniumMap.put("java", javaCode);
            Map<String, Map<String, List<String>>> codeMap = new LinkedHashMap<>();
            codeMap.put("selenium", seleniumMap);
            intentMap.put("code", codeMap);

            // Entities section
            Map<String, List<String>> entitiesMap = new LinkedHashMap<>();
            System.out.println("Insira o target entities (linha vazia encerra o campo):");
            List<String> targets = new ArrayList<>();
            while (!(line = scanner.nextLine()).isEmpty()) {
                targets.add(line);
            }
            entitiesMap.put("target", targets);
            entitiesMap.put("related", new ArrayList<>()); // If related entities needed, you can add here similarly.
            intentMap.put("entities", entitiesMap);

            intentMap.put("values", new LinkedHashMap<>());

            intentsList.add(intentMap);

            System.out.println("Deseja inserir outro intent? (yes/no):");
            addMoreIntents = "yes".equalsIgnoreCase(scanner.nextLine());
        } while (addMoreIntents);

        Map<String, Object> context = new LinkedHashMap<>();
        context.put("context", contextMap);
        contextMap.put("intents", intentsList);

        // Convert to JSON String
        String jsonString = toJsonString(context);
        System.out.println(jsonString);

        // Write to a text file
        writeToFile("output.txt", jsonString);

        // Close the scanner
        scanner.close();
    }

    private static String toJsonString(Map<String, Object> map) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            json.append(quote(entry.getKey())).append(": ");
            if (entry.getValue() instanceof String) {
                json.append(quote(entry.getValue().toString()));
            } else if (entry.getValue() instanceof Map) {
                json.append(toJsonString((Map<String, Object>) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                json.append(toJsonList((List<?>) entry.getValue()));
            } else {
                json.append(entry.getValue().toString());
            }
            if (it.hasNext()) json.append(",");
            json.append("\n");
        }
        json.append("}\n");
        return json.toString();
    }

    private static String toJsonList(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            if (item instanceof String) {
                json.append(quote(item.toString()));
            } else if (item instanceof Map) {
                json.append(toJsonString((Map<String, Object>) item));
            } else {
                json.append(item.toString());
            }
            if (it.hasNext()) json.append(",");
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    private static String quote(String string) {
        return "\"" + string.replace("\"", "\\\"") + "\"";
    }

    private static void writeToFile(String fileName, String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(content);
            System.out.println("JSON saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
