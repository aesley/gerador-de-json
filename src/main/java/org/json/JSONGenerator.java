package org.json;
// quase no caminho certo
import java.util.*;

public class JSONGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> imports = new HashMap<>();
        Map<String, Object> source = new HashMap<>();
        List<Map<String, Object>> intents = new ArrayList<>();

        // Preencha as informações de imports Java
        List<String> javaImports = new ArrayList<>();
        while (true) {
            System.out.print("Vamos iniciar colocando os Imports");
            System.out.print("Import Java (ex: import org.example.SomeClass;): ");
            String importStatement = scanner.nextLine();
            if (importStatement.isEmpty()) {
                break;
            }
            javaImports.add(importStatement);
        }
        imports.put("java", javaImports);

        // Preencha as informações do contexto
        System.out.print("Coloque a URL da fonte");
        System.out.print("URL da fonte (ex: https://www.google.com/): ");
        String url = scanner.nextLine();
        source.put("url: ", url);
        source.put("language", "pt-br: ");

        // Preencha informações sobre intenções
        while (true) {
            Map<String, Object> intent = new HashMap<>();
            System.out.print("Preencha informações sobre intents");
            System.out.print("Intent: ");
            String intentName = scanner.nextLine();
            intent.put("intent: ", intentName);

            System.out.print("IntentGenerated: ");
            String intentGenerated = scanner.nextLine();
            intent.put("intent-generated: ", intentGenerated);

            Map<String, Object> code = new HashMap<>();
            Map<String, Object> selenium = new HashMap<>();
            List<String> javaCode = new ArrayList<>();
            System.out.print("Preencha informações sobre o codigo java feito com selenium");
            System.out.println("código java ex: driver.findElement(By.id(\"APjFqb\")).sendKeys(\"atlantico\"); apenas de enter para finalizar: ");
            while (true) {
                String javaLine = scanner.nextLine();
                if (javaLine.isEmpty()) {
                    break;
                }
                javaCode.add(javaLine);
            }
            selenium.put("java: ", javaCode);
            code.put("selenium: ", selenium);
            intent.put("code: ", code);

            Map<String, Object> entities = new HashMap<>();
            List<Map<String, Object>> target = new ArrayList<>();
            System.out.print("Preencha informações sobre entities");
            System.out.print("Entities - Target ex: By.id: APjFqb: ");
            String entityType = scanner.nextLine();
            Map<String, Object> targetEntity = new HashMap<>();
            targetEntity.put(entityType, scanner.nextLine());
            target.add(targetEntity);
            entities.put("target: ", target);

            entities.put("related: ", new ArrayList<>());
            entities.put("values: ", new HashMap<>());
            intent.put("entities: ", entities);

            intents.add(intent);

            System.out.print("Deseja adicionar outra intenção? (s = sim/n = não): ");

            String addAnotherIntent = scanner.nextLine();
            if (!addAnotherIntent.equalsIgnoreCase("s")) {
                break;
            }
        }

        // Construa o contexto completo
        context.put("imports: ", imports);
        context.put("source: ", source);
        context.put("intents: ", intents);

        // Construa o JSON completo
        Map<String, Object> json = new HashMap<>();
        json.put("context: ", context);

        // Exiba o JSON gerado no formato esperado
        System.out.println("{");
        System.out.println("    \"context\": {");
        System.out.println("        \"imports\": " + imports + ",");
        System.out.println("        \"source\": " + source + ",");
        System.out.println("        \"intents\": " + intents);
        System.out.println("    }");
        System.out.println("}");
    }
}

