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
import com.sabre.oss.yare.serializer.json.RuleToJsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class OperatorSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeEqualOperator() throws JsonProcessingException {
        Operator operator = new Operator()
                .withType("test-type")
                .withExpressions(Collections.singletonList(
                        new Expression().withValue(new Value().withValue("test-value"))
                ));

        String serialized = objectMapper.writeValueAsString(operator);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"test-type\": [" +
                "    {" +
                "       \"value\": \"test-value\"" +
                "    }" +
                "  ]" +
                "}");
    }

    @Test
    void shouldProperlyDeserializeEqualOperator() throws IOException {
        String serialized = "" +
                "{" +
                "  \"test-type\": [" +
                "    {" +
                "       \"value\": \"test-value\"" +
                "    }," +
                "    {" +
                "       \"values\": [" +
                "       {" +
                "           \"value\": \"test-value-1\"," +
                "           \"type\": \"test-type-1\"" +
                "       }" +
                "       ]," +
                "       \"type\": \"test-type\"" +
                "    }" +
                "  ]" +
                "}";

        Operator deserialized = objectMapper.readValue(serialized, Operator.class);

        Operator expected = new Operator()
                .withType("test-type")
                .withExpressions(Arrays.asList(
                        new Expression()
                                .withValue(new Value().withValue("test-value").withType("java.lang.String"))
                                .withValues(new Values().withType(null)),
                        new Expression()
                                .withValue(new Value().withType("test-type"))
                                .withValues(new Values().withValues(Collections.singletonList(new Value().withValue("test-value-1").withType("test-type-1"))).withType("test-type"))
                ));
        assertThat(deserialized).isEqualToComparingFieldByFieldRecursively(expected);
    }
}
