/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.http.spring.boot.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link HttpClientParams }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("HttpClientParams Tests")
class HttpClientParamsTest {

    @Test
    @DisplayName("Enum values are accessible")
    void testEnumValues() {
        HttpClientParams[] values = HttpClientParams.values();
        assertThat(values).isNotEmpty();
        assertThat(values.length).isGreaterThanOrEqualTo(12);
    }

    @Test
    @DisplayName("Enum constant has name and default value")
    void testEnumConstantProperties() {
        HttpClientParams param = HttpClientParams.HTTP_CONNECTION_KEEPALIVE;
        assertThat(param.getName()).isEqualTo("http.connection.keepAlive");
        assertThat(param.getDefault()).isEqualTo("30000");
    }

    @Test
    @DisplayName("valueOf resolves by name")
    void testValueOf() {
        HttpClientParams param = HttpClientParams.valueOf("HTTP_CONNECTION_KEEPALIVE");
        assertThat(param).isSameAs(HttpClientParams.HTTP_CONNECTION_KEEPALIVE);
    }
}
