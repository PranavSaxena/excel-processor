package com.excelprocessor.model;

import java.util.List;

public class RuleNode {
    private String operator;
    private List<String> values;

    public RuleNode(String operator, List<String> values) {
        this.operator = operator;
        this.values = values;
    }

    public String getOperator() { 
    	return operator; 
    }
    
    public List<String> getValues() { 
    	return values; 
    }
}
