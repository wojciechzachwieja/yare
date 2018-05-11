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

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class ValueSerializationTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = RuleToJsonConverter.getObjectMapper();
    }

    @Test
    void shouldProperlySerializeValue() throws JsonProcessingException {
        Value value = new Value()
                .withValue("test-value")
                .withType("test-type");

        String serialized = objectMapper.writeValueAsString(value);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"value\" : \"test-value\"," +
                "  \"type\" : \"test-type\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeValueWhenTypeIsString() throws JsonProcessingException {
        Value value = new Value()
                .withValue("test-value")
                .withType("java.lang.String");

        String serialized = objectMapper.writeValueAsString(value);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"value\" : \"test-value\"" +
                "}");
    }

    @Test
    void shouldProperlySerializeValueWhenValueIsString() throws JsonProcessingException {
        Value value = new Value()
                .withValue("java.lang.String")
                .withType("test-type");

        String serialized = objectMapper.writeValueAsString(value);

        assertThatJson(serialized).isEqualTo("" +
                "{" +
                "  \"value\" : \"java.lang.String\"," +
                "  \"type\" : \"test-type\"" +
                "}");
    }

    @Test
    void shouldProperlyDeserializeValue() throws IOException {
        String serialized = "" +
                "{" +
                "  \"value\" : \"test-value\"," +
                "  \"type\" : \"test-type\"" +
                "}";

        Value deserialized = objectMapper.readValue(serialized, Value.class);

        Value expected = new Value()
                .withValue("test-value")
                .withType("test-type");
        assertThat(deserialized).isEqualTo(expected);
    }

    @Test
    void shouldProperlyDeserializeValueWithoutType() throws IOException {
        String serialized = "" +
                "{" +
                "  \"value\" : \"test-value\"" +
                "}";

        Value deserialized = objectMapper.readValue(serialized, Value.class);

        Value expected = new Value()
                .withValue("test-value")
                .withType("java.lang.String");
        assertThat(deserialized).isEqualTo(expected);
    }

    @Test
    void shouldProperlyDeserializeValueWhenValueIsString() throws IOException {
        String serialized = "" +
                "{" +
                "  \"value\" : \"java.lang.String\"," +
                "  \"type\" : \"test-type\"" +
                "}";

        Value deserialized = objectMapper.readValue(serialized, Value.class);

        Value expected = new Value()
                .withValue("java.lang.String")
                .withType("test-type");
        assertThat(deserialized).isEqualTo(expected);
    }
}
