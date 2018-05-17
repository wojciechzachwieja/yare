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
import java.util.Collections;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class ExpressionSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeExpressionWithValue() throws JsonProcessingException {
        Expression expression = new Expression()
                .withValue(new Value().withValue("value-value").withType("value-type"));

        String serialized = objectMapper.writeValueAsString(expression);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"value\": \"value-value\"," +
                "  \"type\": \"value-type\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeExpressionWithValues() throws JsonProcessingException {
        Expression expression = new Expression()
                .withValues(new Values()
                        .withValues(Collections.singletonList(
                                new Value().withValue("value-value").withType("value-type")))
                        .withType("values-type")
                );

        String serialized = objectMapper.writeValueAsString(expression);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"values\": [" +
                "    {" +
                "      \"value\": \"value-value\"," +
                "      \"type\": \"value-type\"" +
                "    }" +
                "  ]," +
                "  \"type\": \"values-type\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeExpressionWithFunction() throws JsonProcessingException {
        Expression expression = new Expression()
                .withFunction(new Function()
                        .withName("function-name")
                        .withParameters(Collections.singletonList(
                                new Parameter()
                                        .withValue(new Value().withValue("value-value").withType("value-type"))
                        )));

        String serialized = objectMapper.writeValueAsString(expression);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"function\": {" +
                "    \"name\": \"function-name\"," +
                "    \"parameters\": [" +
                "      {" +
                "        \"value\": \"value-value\"," +
                "        \"type\": \"value-type\"" +
                "      }" +
                "    ]" +
                "  }" +
                "}");
    }

    @Test
    void shouldProperlySerializeExpressionWithOperator() throws JsonProcessingException {
        Expression expression = new Expression()
                .withOperator(new Operator()
                        .withType("operator-type")
                        .withExpressions(Collections.singletonList(
                                new Expression()
                                        .withValue(new Value().withValue("value-value").withType("value-type"))
                        )));

        String serialized = objectMapper.writeValueAsString(expression);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"operator\": {" +
                "    \"operator-type\": [" +
                "      {" +
                "        \"value\": \"value-value\"," +
                "        \"type\": \"value-type\"" +
                "      }" +
                "    ]" +
                "  }" +
                "}");
    }

    @Test
    void shouldProperlyDeserializeExpressionWithValue() throws IOException {
        String serialized = "" +
                "{" +
                "  \"value\": \"value-value\"," +
                "  \"type\": \"value-type\"" +
                "}";

        Expression deserialized = objectMapper.readValue(serialized, Expression.class);

        Expression expected = new Expression()
                .withValue(new Value().withValue("value-value").withType("value-type"))
                .withValues(new Values().withType("value-type"));
        assertThat(deserialized).isEqualTo(expected);
    }

    @Test
    void shouldProperlyDeserializeExpressionWithValues() throws IOException {
        String serialized = "" +
                "{" +
                "  \"values\": [" +
                "    {" +
                "      \"value\": \"value-value\"," +
                "      \"type\": \"value-type\"" +
                "    }" +
                "  ]," +
                "  \"type\": \"values-type\"" +
                "}";

        Expression deserialized = objectMapper.readValue(serialized, Expression.class);

        Expression expected = new Expression()
                .withValue(new Value().withType("values-type"))
                .withValues(new Values()
                        .withValues(Collections.singletonList(
                                new Value().withValue("value-value").withType("value-type")))
                        .withType("values-type"));
        assertThat(deserialized).isEqualTo(expected);
    }

    @Test
    void shouldProperlyDeserializeExpressionWithFunction() throws IOException {
        String serialized = "" +
                "{" +
                "  \"function\": {" +
                "    \"name\": \"function-name\"," +
                "    \"parameters\": [" +
                "      {" +
                "        \"value\": \"value-value\"," +
                "        \"type\": \"value-type\"" +
                "      }" +
                "    ]" +
                "  }" +
                "}";

        Expression deserialized = objectMapper.readValue(serialized, Expression.class);

        Expression expected = new Expression()
                .withValue(new Value())
                .withValues(new Values())
                .withFunction(new Function()
                        .withName("function-name")
                        .withParameters(Collections.singletonList(
                                new Parameter()
                                        .withValue(new Value().withValue("value-value").withType("value-type"))
                                        .withValues(new Values().withType("value-type"))
                        )));
        assertThat(deserialized).isEqualTo(expected);
    }

    @Test
    void shouldProperlyDeserializeExpressionWithOperator() throws IOException {
        String serialized = "" +
                "{" +
                "  \"operator\": {" +
                "    \"operator-type\": [" +
                "      {" +
                "        \"value\": \"value-value\"," +
                "        \"type\": \"value-type\"" +
                "      }" +
                "    ]" +
                "  }" +
                "}";

        Expression deserialized = objectMapper.readValue(serialized, Expression.class);

        Expression expected = new Expression()
                .withValue(new Value())
                .withValues(new Values())
                .withOperator(new Operator()
                        .withType("operator-type")
                        .withExpressions(Collections.singletonList(
                                new Expression()
                                        .withValue(new Value().withValue("value-value").withType("value-type"))
                                        .withValues(new Values().withType("value-type"))
                        )));
        assertThat(deserialized).isEqualTo(expected);
    }
}
