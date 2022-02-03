package de.mydomain.drools;

import de.mydomain.drools.model.Condition;
import de.mydomain.drools.model.DroolsUtility;
import de.mydomain.drools.model.Product;
import de.mydomain.drools.model.Rule;
import org.kie.api.runtime.StatelessKieSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {

        List<Rule> rules = new ArrayList<>();

        rules.add(createDiscountOverpriced());
        rules.add(createDiscountSoonDueDate());
        rules.add(createDiscountBeans());

        DroolsUtility utility = new DroolsUtility();
        StatelessKieSession session = utility.loadSession(rules, "drools/templates/Product.drl");

        Product blackBeans = new Product("Black Beans", 2.20, "30/12/2017");
        Product cannelliniBeans = new Product("Cannellini Beans", 4.15, "05/02/2018");
        Product kidneyBeans = new Product("Kidney Beans", 2.05, "20/11/2017");
        Product rice = new Product("Rice", 1.10, "28/10/2017");
        Product milk = new Product("Milk", 3.50, "10/11/2017");

        System.out.println("Applying over " + rice.getName() + " with price $" + rice.getPrice() + "...");
        session.setGlobal("product", rice);
        session.execute(rice);
        System.out.println("Price after review: $" + rice.getPrice());

        System.out.println("\n\n");

        System.out.println("Applying over " + blackBeans.getName() + " with price $" + blackBeans.getPrice() + "...");
        session.setGlobal("product", blackBeans);
        session.execute(blackBeans);
        System.out.println("Price after review: $" + blackBeans.getPrice());

        System.out.println("\n\n");

        System.out.println("Applying over " + milk.getName() + " with price $" + milk.getPrice() + "...");
        session.setGlobal("product", milk);
        session.execute(milk);
        System.out.println("Price after review: $" + milk.getPrice());

        System.out.println("\n\n");

        System.out.println("Applying over " + kidneyBeans.getName() + " with price $" + kidneyBeans.getPrice() + "...");
        session.setGlobal("product", kidneyBeans);
        session.execute(kidneyBeans);
        System.out.println("Price after review: $" + kidneyBeans.getPrice());

        System.out.println("\n\n");

        System.out.println("Applying over " + cannelliniBeans.getName() + " with price $" + cannelliniBeans.getPrice() + "...");
        session.setGlobal("product", cannelliniBeans);
        session.execute(cannelliniBeans);
        System.out.println("Price after review: $" + cannelliniBeans.getPrice());
    }

    private static Rule createDiscountOverpriced() {

        Rule rule = new Rule("Give some discount on overpriced");
        rule.setDataObject(Product.class.getName());

        Condition condition = new Condition();
        condition.setProperty("price");
        condition.setOperator(Condition.Operator.GREATER_THAN_OR_EQUAL_TO);
        condition.setValue(4.0);

        rule.setCondition(condition);
        rule.setAction("10");

        return rule;
    }

    private static Rule createDiscountSoonDueDate() throws Exception {
        Rule rule = new Rule("Apply discount on all soon due date");
        rule.setDataObject(Product.class.getName());

        Condition greaterThan = new Condition();
        greaterThan.setProperty("dueDate");
        greaterThan.setOperator(Condition.Operator.GREATER_THAN);
        greaterThan.setValue((new SimpleDateFormat("dd/MM/yyyy").parse("23/10/2017")));

        Condition lessThan = new Condition();
        lessThan.setProperty("dueDate");
        lessThan.setOperator(Condition.Operator.LESS_THAN);
        lessThan.setValue((new SimpleDateFormat("dd/MM/yyyy").parse("30/10/2017")));

        rule.setConditions(Arrays.asList(greaterThan, lessThan));
        rule.setAction("45");

        return rule;
    }

    private static Rule createDiscountBeans() {
        Rule rule = new Rule("Discounting on all beans");
        rule.setDataObject(Product.class.getName());
        rule.addCondition("name", Condition.Operator.CONTAINS, "Beans");
        rule.setAction("5");
        return rule;
    }
}