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

import java.util.Arrays;
import java.util.Collections;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

class ParameterSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeValueParameter() throws JsonProcessingException {
        Parameter parameter = new Parameter()
                .withName("test-name")
                .withValue(new Value().withValue("test-value").withType("test-type"));

        String serialized = objectMapper.writeValueAsString(parameter);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"name\": \"test-name\"," +
                "  \"value\": \"test-value\"," +
                "  \"type\": \"test-type\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeValueParameterWhenTypeIsString() throws JsonProcessingException {
        Parameter parameter = new Parameter()
                .withName("test-name")
                .withValue(new Value().withValue("test-value").withType("java.lang.String"));

        String serialized = objectMapper.writeValueAsString(parameter);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"name\": \"test-name\"," +
                "  \"value\": \"test-value\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeValuesParameter() throws JsonProcessingException {
        Parameter parameter = new Parameter()
                .withName("test-name")
                .withValues(new Values()
                        .withValues(Arrays.asList(
                                new Value().withValue("test-value-1").withType("test-type-1"),
                                new Value().withValue("test-value-2").withType("java.lang.String"),
                                new Value().withValue("java.lang.String").withType("test-type-3")))
                        .withType("test-type"));

        String serialized = objectMapper.writeValueAsString(parameter);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"name\": \"test-name\"," +
                "  \"values\": [{" +
                "      \"value\": \"test-value-1\"," +
                "      \"type\": \"test-type-1\"" +
                "  }, {" +
                "      \"value\": \"test-value-2\"" +
                "  }, {" +
                "      \"value\": \"java.lang.String\"," +
                "      \"type\": \"test-type-3\"" +
                "  }]," +
                "  \"type\" : \"test-type\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeFunctionParameter() throws JsonProcessingException {
        Parameter parameter = new Parameter()
                .withName("test-name-1")
                .withFunction(new Function()
                        .withName("test-name-2")
                        .withParameters(Collections.singletonList(
                                new Parameter()
                                        .withName("test-name-3")
                                        .withValue(new Value()
                                                .withValue("test-value")
                                                .withType("java.lang.String")
                                        )
                                )
                        )
                );

        String serialized = objectMapper.writeValueAsString(parameter);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"name\": \"test-name-1\"," +
                "  \"function\": {" +
                "    \"name\": \"test-name-2\"," +
                "    \"parameters\": [{" +
                "      \"name\": \"test-name-3\"," +
                "      \"value\": \"test-value\"" +
                "    }]" +
                "  }" +
                "}");
    }
}
