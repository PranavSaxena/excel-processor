package com.excelprocessor.util;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RuleConverter {

    public static String convertToRule(String requiredTasks) {
        if (requiredTasks == null || requiredTasks.trim().isEmpty()) {
            return "";
        }

        requiredTasks = requiredTasks.replaceAll("\\s+", "");
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Case 1: AND/Comma only (No OR)
            if ((requiredTasks.contains("&") || requiredTasks.contains(",")) && !requiredTasks.contains("|")) {
                String[] parts = requiredTasks.split("&|,");
                Map<String, Object> map = new HashMap<>();
                map.put("missing", Arrays.asList(parts));
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            }
            // Case 2: OR only (No AND)
            else if (requiredTasks.contains("|") && !(requiredTasks.contains("&") || requiredTasks.contains(","))) {
                String[] parts = requiredTasks.split("\\|");
                Map<String, Object> map = new HashMap<>();
                map.put("missing_some", Arrays.asList(1, appendDummy(parts)));
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            }
            // Case 3: OR + AND mixed
            else if (requiredTasks.contains("|") && (requiredTasks.contains("&") || requiredTasks.contains(","))) {
                String[] orAndParts = requiredTasks.split("\\|");

                if (orAndParts.length == 2) {
                    String left = orAndParts[0];
                    String right = orAndParts[1];

                    Map<String, Object> leftCondition, rightCondition;

                    if (left.contains("&") || left.contains(",")) {
                        leftCondition = new HashMap<>();
                        leftCondition.put("missing", Arrays.asList(left.split("&|,")));
                    } else {
                        leftCondition = new HashMap<>();
                        leftCondition.put("missing_some", Arrays.asList(1, Arrays.asList(left, "dummy")));
                    }

                    if (right.contains("&") || right.contains(",")) {
                        rightCondition = new HashMap<>();
                        rightCondition.put("missing", Arrays.asList(right.split("&|,")));
                    } else {
                        rightCondition = new HashMap<>();
                        rightCondition.put("missing_some", Arrays.asList(1, Arrays.asList(right, "dummy")));
                    }

                    Map<String, Object> ifMap = new HashMap<>();
                    ifMap.put("if", Arrays.asList(leftCondition, rightCondition, "OK"));
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ifMap);
                }
                else {
                    String[] lastAndSplit = orAndParts[orAndParts.length - 1].split("&|,");
                    List<String> rightList = Arrays.asList(lastAndSplit);

                    List<String> orList = new ArrayList<>(Arrays.asList(Arrays.copyOf(orAndParts, orAndParts.length - 1)));

                    Map<String, Object> leftOrCondition = new HashMap<>();
                    leftOrCondition.put("missing_some", Arrays.asList(1, appendDummy(orList.toArray(new String[0]))));

                    Map<String, Object> rightAndCondition = new HashMap<>();
                    rightAndCondition.put("missing", rightList);

                    Map<String, Object> ifMap = new HashMap<>();
                    ifMap.put("if", Arrays.asList(leftOrCondition, rightAndCondition, "OK"));

                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ifMap);
                }
            }
            // Case 4: Single value
            else {
                Map<String, Object> map = new HashMap<>();
                map.put("missing_some", Arrays.asList(1, Arrays.asList(requiredTasks, "dummy")));
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            }
        } catch (Exception e) {
            return "Error in Rule Conversion: " + e.getMessage();
        }
    }

	private static List<String> appendDummy(String[] arr) {
		List<String> list = new ArrayList<>(Arrays.asList(arr));
		list.add("dummy");
		return list;
	}
}
