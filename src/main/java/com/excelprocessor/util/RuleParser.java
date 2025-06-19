package com.excelprocessor.util;

import com.excelprocessor.model.RuleNode;

import java.util.Arrays;
import java.util.List;

public class RuleParser {

    public static RuleNode parse(String task) {
        if (task.contains("&") || task.contains(",")) {
            List<String> parts = Arrays.asList(task.split("&|,"));
            return new RuleNode("AND", parts);
        } else if (task.contains("|")) {
            List<String> parts = Arrays.asList(task.split("\\|"));
            return new RuleNode("OR", parts);
        } else {
            return new RuleNode("SINGLE", List.of(task));
        }
    }
}
