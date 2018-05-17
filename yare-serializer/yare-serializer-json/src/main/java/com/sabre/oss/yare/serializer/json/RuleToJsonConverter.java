/*
 * MIT License
 *
 * Copyright 2018 Sabre GLBL Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sabre.oss.yare.serializer.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sabre.oss.yare.core.model.Rule;
import com.sabre.oss.yare.model.converter.RuleConversionException;
import com.sabre.oss.yare.model.converter.RuleConverter;
import com.sabre.oss.yare.serializer.json.mapper.converter.json.ToJsonConverter;
import com.sabre.oss.yare.serializer.json.mapper.converter.rule.ToRuleConverter;

import java.io.IOException;

public class RuleToJsonConverter implements RuleConverter {
    private static final ToJsonConverter toJsonConverter = new ToJsonConverter();
    private static final ToRuleConverter TO_RULE_CONVERTER = new ToRuleConverter();

    private final ObjectMapper objectMapper;

    public RuleToJsonConverter() {
        this.objectMapper = getObjectMapper();
    }

    public RuleToJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Override
    public String marshal(Rule rule) throws RuleConversionException {
        try {
            com.sabre.oss.yare.serializer.json.model.Rule jsonRule = toJsonConverter.convert(rule);
            return objectMapper.writeValueAsString(jsonRule);
        } catch (JsonProcessingException e) {
            throw new RuleConversionException("Error occured during rule serialization", e);
        }
    }

    @Override
    public Rule unmarshal(String value) throws RuleConversionException {
        try {
            com.sabre.oss.yare.serializer.json.model.Rule jsonRule = objectMapper.readValue(value, com.sabre.oss.yare.serializer.json.model.Rule.class);
            return TO_RULE_CONVERTER.convert(jsonRule);
        } catch (IOException e) {
            throw new RuleConversionException("Error occured during rule serialization", e);
        }
    }
}
