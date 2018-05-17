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

package com.sabre.oss.yare.serializer.json.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabre.oss.yare.serializer.json.ResourceUtils;
import com.sabre.oss.yare.serializer.json.RuleToJsonConverter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

class RuleSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeRule() throws JsonProcessingException {
        Rule rule = new Rule()
                .withAttributes(Arrays.asList(
                        new Attribute().withName("attribute-name-1").withValue("attribute-value-1").withType("attribute-type-1"),
                        new Attribute().withName("attribute-name-2").withValue("attribute-value-2").withType("attribute-type-2"),
                        new Attribute().withName("attribute-name-3").withValue("attribute-value-3").withType("attribute-type-3")
                ))
                .withFacts(Arrays.asList(
                        new Fact().withName("fact-name-1").withType("fact-type-1"),
                        new Fact().withName("fact-name-2").withType("fact-type-2")
                ))
                .withPredicate(new Expression()
                        .withOperator(new Operator()
                                .withType("operator-type-1")
                                .withExpressions(Arrays.asList(
                                        new Expression()
                                                .withOperator(new Operator()
                                                        .withType("operator-type-2")
                                                        .withExpressions(Arrays.asList(
                                                                new Expression()
                                                                        .withValue(new Value().withValue("value-value-1")),
                                                                new Expression()
                                                                        .withValue(new Value().withValue("value-value-2").withType("value-type-2"))
                                                        ))),
                                        new Expression()
                                                .withOperator(new Operator()
                                                        .withType("operator-type-3")
                                                        .withExpressions(Arrays.asList(
                                                                new Expression().withFunction(new Function()
                                                                        .withName("function-name")
                                                                        .withParameters(Arrays.asList(
                                                                                new Parameter()
                                                                                        .withName("parameter-name-1")
                                                                                        .withValue(new Value().withValue("value-value-3")),
                                                                                new Parameter()
                                                                                        .withName("parameter-name-2")
                                                                                        .withValues(new Values()
                                                                                                .withType("values-type")
                                                                                                .withValues(Collections.singletonList(
                                                                                                        new Value().withValue("value-value-4").withType("value-type-4")
                                                                                                )))
                                                                        ))),
                                                                new Expression()
                                                                        .withOperator(new Operator()
                                                                                .withType("operator-type-3")
                                                                                .withExpressions(Collections.singletonList(
                                                                                        new Expression()
                                                                                                .withValue(new Value().withValue("value-value-5"))
                                                                                )))
                                                        )))
                                )))
                )
                .withActions(Collections.singletonList(
                        new Action()
                                .withName("action-name")
                                .withParameters(Arrays.asList(
                                        new Parameter()
                                                .withName("parameter-name-3")
                                                .withValues(new Values()
                                                        .withType("values-type-3")
                                                        .withValues(Collections.emptyList()))
                                ))
                ));

        String serialized = objectMapper.writeValueAsString(rule);

        String expected = StringUtils.substringAfterLast(ResourceUtils.getResourceAsString("/ruleInJsonModel.json"), "*/");
        assertThatJson(serialized).isEqualTo(expected);
    }
}
