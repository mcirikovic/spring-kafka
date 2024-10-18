/*
 * Copyright 2021-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.kafka.test.condition;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@SpringJUnitConfig(WithNestedClassContextTests.Config.class)
class WithNestedClassContextTests {

	private static final AtomicInteger counter = new AtomicInteger();

	@Autowired
	private TestClass outer;

	@Nested
	class NestedClass {

		@Test
		void equalsInjected(@Autowired TestClass inner) {
			assertThat(inner).isEqualTo(outer);
		}

		@Test
		void equalsSize(@Autowired List<TestClass> classes) {
			assertThat(classes).hasSize(1);
		}

		@Test
		void equalsCount() {
			assertThat(counter.get()).isEqualTo(1);
		}
	}

	public static class TestClass {
	}

	@Configuration
	static class Config {
		@Bean
		public TestClass testClass() {
			counter.incrementAndGet();
			return new TestClass();
		}
	}
}
