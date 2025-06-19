package com.excelprocessor.util;

import com.excelprocessor.model.RuleNode;

import java.util.*;

public class RuleJsonMapper {

    public static Map<String, Object> mapToJson(RuleNode node) {
        Map<String, Object> map = new HashMap<>();
        switch (node.getOperator()) {
            case "AND":
                map.put("missing", node.getValues());
                break;
            case "OR":
                map.put("missing_some", Arrays.asList(1, appendDummy(node.getValues())));
                break;
            case "SINGLE":
                map.put("missing_some", Arrays.asList(1, appendDummy(node.getValues())));
                break;
        }
        return map;
    }

    private static List<String> appendDummy(List<String> list) {
        List<String> newList = new ArrayList<>(list);
        newList.add("dummy");
        return newList;
    }
}
