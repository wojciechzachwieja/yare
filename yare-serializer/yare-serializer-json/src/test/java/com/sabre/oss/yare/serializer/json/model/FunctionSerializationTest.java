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

class FunctionSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeFunction() throws JsonProcessingException {
        Function function = new Function()
                .withName("function-name")
                .withParameters(Arrays.asList(
                        new Parameter()
                                .withName("parameter-name-1")
                                .withValue(new Value()
                                        .withValue("value-value-1")
                                        .withType("value-type-1")),
                        new Parameter()
                                .withName("parameter-name-2")
                                .withValues(new Values()
                                        .withValues(Collections.singletonList(
                                                new Value()
                                                        .withValue("value-value-2")
                                                        .withType("value-type-2")))
                                        .withType("values-type")),
                        new Parameter()
                                .withName("parameter-name-3")
                                .withOperator(new Operator()
                                        .withType("operator-type")
                                        .withExpressions(Collections.emptyList()))
                ));

        String serialized = objectMapper.writeValueAsString(function);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"name\": \"function-name\"," +
                "  \"parameters\": [" +
                "    {" +
                "      \"name\": \"parameter-name-1\"," +
                "      \"value\": \"value-value-1\"," +
                "      \"type\": \"value-type-1\"" +
                "    }," +
                "    {" +
                "      \"name\": \"parameter-name-2\"," +
                "      \"values\": [" +
                "        {" +
                "          \"value\": \"value-value-2\"," +
                "          \"type\": \"value-type-2\"" +
                "        }" +
                "      ]," +
                "      \"type\": \"values-type\"" +
                "    }," +
                "    {" +
                "      \"name\": \"parameter-name-3\"," +
                "      \"operator\": {" +
                "        \"operator-type\": [" +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}");
    }

    @Test
    void shouldProperlyDeserializeFunction() throws IOException {
        String serialized = "" +
                "{" +
                "  \"name\": \"function-name\"," +
                "  \"parameters\": [" +
                "    {" +
                "      \"name\": \"parameter-name-1\"," +
                "      \"value\": \"value-value-1\"," +
                "      \"type\": \"value-type-1\"" +
                "    }," +
                "    {" +
                "      \"name\": \"parameter-name-2\"," +
                "      \"values\": [" +
                "        {" +
                "          \"value\": \"value-value-2\"," +
                "          \"type\": \"value-type-2\"" +
                "        }" +
                "      ]," +
                "      \"type\": \"values-type\"" +
                "    }," +
                "    {" +
                "      \"name\": \"parameter-name-3\"," +
                "      \"operator\": {" +
                "        \"operator-type\": [" +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}";

        Function deserialized = objectMapper.readValue(serialized, Function.class);

        Function expected = new Function()
                .withName("function-name")
                .withParameters(Arrays.asList(
                        new Parameter()
                                .withName("parameter-name-1")
                                .withValue(new Value()
                                        .withValue("value-value-1")
                                        .withType("value-type-1"))
                                .withValues(new Values().withType("value-type-1")),
                        new Parameter()
                                .withName("parameter-name-2")
                                .withValue(new Value().withType("values-type"))
                                .withValues(new Values()
                                        .withValues(Collections.singletonList(
                                                new Value()
                                                        .withValue("value-value-2")
                                                        .withType("value-type-2")
                                        ))
                                        .withType("values-type")),
                        new Parameter()
                                .withName("parameter-name-3")
                                .withValue(new Value())
                                .withValues(new Values())
                                .withOperator(new Operator()
                                        .withType("operator-type")
                                        .withExpressions(Collections.emptyList()))
                ));
        assertThat(deserialized).isEqualTo(expected);
    }
}
