package com.excelprocessor.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

public class RuleConverterTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private void assertJsonEquals(String expectedJson, String actualJson) throws Exception {
        JsonNode expectedNode = mapper.readTree(expectedJson);
        JsonNode actualNode = mapper.readTree(actualJson);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    void testAndCondition() throws Exception {
        String input = "016G&060G";
        String expected = "{\"missing\":[\"016G\",\"060G\"]}";
        String actual = RuleConverter.convertToRule(input);
        assertJsonEquals(expected, actual);
    }

    @Test
    void testCommaAndCondition() throws Exception {
        String input = "016L,060L";
        String expected = "{\"missing\":[\"016L\",\"060L\"]}";
        String actual = RuleConverter.convertToRule(input);
        assertJsonEquals(expected, actual);
    }

    @Test
    void testOrCondition() throws Exception {
        String input = "001|002|003";
        String expected = "{\"missing_some\":[1,[\"001\",\"002\",\"003\",\"dummy\"]]}";
        String actual = RuleConverter.convertToRule(input);
        assertJsonEquals(expected, actual);
    }

//    @Test
//    void testMixedOrAndCondition() throws Exception {
//        String input = "016G|060G&008";
//        String expected = "{ \"if\" : [ { \"missing_some\" : [ 1, [ \"016G\", \"060G\", \"dummy\" ] ] }, { \"missing\" : [ \"008\" ] }, \"OK\" ] }";
//        String actual = RuleConverter.convertToRule(input);
//        assertJsonEquals(expected, actual);
//    }

    @Test
    void testMixedAndOrCondition() throws Exception {
        String input = "003&060G|008";
        String expected = "{ \"if\" : [ { \"missing\" : [ \"003\", \"060G\" ] }, { \"missing_some\" : [ 1, [ \"008\", \"dummy\" ] ] }, \"OK\" ] }";
        String actual = RuleConverter.convertToRule(input);
        assertJsonEquals(expected, actual);
    }

    @Test
    void testSingleValue() throws Exception {
        String input = "006";
        String expected = "{\"missing_some\":[1,[\"006\",\"dummy\"]]}";
        String actual = RuleConverter.convertToRule(input);
        assertJsonEquals(expected, actual);
    }

    @Test
    void testNullInput() throws Exception {
        String input = null;
        String expected = "";
        String actual = RuleConverter.convertToRule(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEmptyInput() throws Exception {
        String input = " ";
        String expected = "";
        String actual = RuleConverter.convertToRule(input);
        assertEquals(expected, actual);
    }
}
