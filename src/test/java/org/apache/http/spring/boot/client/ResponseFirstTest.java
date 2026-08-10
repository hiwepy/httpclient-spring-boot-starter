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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link ResponseFirst }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ResponseFirst Tests")
class ResponseFirstTest {

    @Test
    @DisplayName("Annotation is present and has RUNTIME retention")
    void testAnnotationRetention() {
        Retention retention = ResponseFirst.class.getAnnotation(Retention.class);
        assertThat(retention).isNotNull();
        assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("Annotation is a Qualifier")
    void testIsQualifier() {
        assertThat(ResponseFirst.class.isAnnotationPresent(Qualifier.class)).isTrue();
    }

    @Test
    @DisplayName("Annotation class is accessible")
    void testClassAccessible() {
        assertThat(ResponseFirst.class).isNotNull();
        assertThat(ResponseFirst.class.isAnnotation()).isTrue();
    }
}
