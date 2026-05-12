// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.doris.nereids.trees.expressions.functions.scalar;

import org.apache.doris.catalog.FunctionSignature;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.literal.JsonLiteral;
import org.apache.doris.nereids.trees.expressions.literal.StringLiteral;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Feature review UT for {@link JsonbType} scalar function.
 *
 * <p>Scope: {@code json_type} step 7 SOP (UT-JT-053). Asserts:
 * <ul>
 *   <li>{@code getSignatures()} returns exactly one entry (no spurious overloads).</li>
 *   <li>{@code withChildren} with 3 args triggers {@link IllegalArgumentException}
 *       (Preconditions.checkArgument(children.size() == 2)).</li>
 *   <li>{@code withChildren} with 2 args yields a new {@link JsonbType}.</li>
 * </ul>
 *
 * <p>Hard-assert contract: PASS = spec; FAIL = bug reproduced.
 */
public class JsonbTypeFeatureReviewTest {

    // ------------------------------------------------------------------
    // UT-JT-053.a — single signature; no spurious overloads.
    // ------------------------------------------------------------------
    @Test
    public void testSignaturesContainsExactlyOne() {
        JsonbType fn = new JsonbType(new JsonLiteral("{\"k\":1}"), new StringLiteral("k"));
        List<FunctionSignature> sigs = fn.getSignatures();
        Assertions.assertNotNull(sigs);
        Assertions.assertEquals(1, sigs.size(),
                "JsonbType must declare exactly one FunctionSignature");
    }

    // ------------------------------------------------------------------
    // UT-JT-053.b — withChildren(2 args) preserves identity & succeeds.
    // ------------------------------------------------------------------
    @Test
    public void testWithChildrenTwoArgsOk() {
        JsonbType fn = new JsonbType(new JsonLiteral("{\"k\":1}"), new StringLiteral("k"));
        Expression replaced = fn.withChildren(ImmutableList.of(
                new JsonLiteral("{\"k\":2}"), new StringLiteral("k")));
        Assertions.assertTrue(replaced instanceof JsonbType,
                "withChildren(2) must produce JsonbType");
    }

    // ------------------------------------------------------------------
    // UT-JT-053.c — withChildren(3 args) must fail Preconditions check.
    // ------------------------------------------------------------------
    @Test
    public void testWithChildrenThreeArgsFails() {
        JsonbType fn = new JsonbType(new JsonLiteral("{\"k\":1}"), new StringLiteral("k"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> fn.withChildren(ImmutableList.of(
                        new JsonLiteral("{\"k\":2}"), new StringLiteral("k"), new StringLiteral("extra"))),
                "withChildren(3) must fail Preconditions.checkArgument");
    }

    // ------------------------------------------------------------------
    // UT-JT-053.d — withChildren(1 arg) must fail Preconditions check too.
    // ------------------------------------------------------------------
    @Test
    public void testWithChildrenOneArgFails() {
        JsonbType fn = new JsonbType(new JsonLiteral("{\"k\":1}"), new StringLiteral("k"));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> fn.withChildren(ImmutableList.of(new JsonLiteral("{\"k\":2}"))),
                "withChildren(1) must fail Preconditions.checkArgument");
    }
}
