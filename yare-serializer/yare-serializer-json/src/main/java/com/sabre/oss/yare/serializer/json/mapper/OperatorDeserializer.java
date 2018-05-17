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

package com.sabre.oss.yare.serializer.json.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sabre.oss.yare.serializer.json.model.Expression;
import com.sabre.oss.yare.serializer.json.model.Operator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OperatorDeserializer extends JsonDeserializer<Operator> {
    @Override
    public Operator deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        ObjectNode objectNode = parser.getCodec().readTree(parser);
        String type = objectNode.fieldNames().next();
        ArrayNode serializedExpressions = (ArrayNode) objectNode.get(type);
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        List<Expression> expressions = getExpressionList(serializedExpressions, objectMapper);
        return new Operator().withType(type).withExpressions(expressions);
    }

    private List<Expression> getExpressionList(ArrayNode expressionsNode, ObjectMapper objectMapper) throws JsonProcessingException {
        List<Expression> expressions = new ArrayList<>();
        for (JsonNode expressionNode : (Iterable<JsonNode>) expressionsNode::elements) {
            Expression expression = objectMapper.treeToValue(expressionNode, Expression.class);
            expressions.add(expression);
        }
        return expressions;
    }
}