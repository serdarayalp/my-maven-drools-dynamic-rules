package de.mydomain.drools.model;

import org.drools.core.spi.KnowledgeHelper;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DroolsUtility {

    public StatelessKieSession loadSession(List<Rule> rules, String templatePath)
            throws Exception {
        List<Map<String, Object>> maps = new ArrayList<>(rules.size());

        for (Rule rule : rules) {
            maps.add(rule.asMap());
        }

        return loadSession(templatePath, maps);
    }

    private StatelessKieSession loadSession(String templatePath, List<Map<String, Object>> rulesAsParameters) throws Exception {
        ObjectDataCompiler compiler = new ObjectDataCompiler();

        // Compiles the list of rules using the template to create a readable Drools Rules Language
        String drl = compiler.compile(rulesAsParameters,
                Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath));

        // Ergebnis ausgeben
        System.out.println("\n\n");
        System.out.println(drl);

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/drools/templates/rule.drl", drl);
        kieServices.newKieBuilder(kieFileSystem).buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        StatelessKieSession statelessKieSession = kieContainer.getKieBase().newStatelessKieSession();

        return statelessKieSession;
    }

    public static void logger(final KnowledgeHelper helper) {

        System.out.println("Triggered rule: " + helper.getRule().getName());

        if (helper.getMatch() != null && helper.getMatch().getObjects() != null) {
            for (Object object : helper.getMatch().getObjects()) {
                System.out.println("Data object: " + object);
            }
        }
    }
}