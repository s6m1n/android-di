package com.example.di.tobe

import com.example.di.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TestRepository

interface DataSource

class TestDataSource : DataSource

class TestRepository2(
    @Inject testDataSource: TestDataSource,
)

class InjectorTest {
    @Test
    fun `inject 메서드는 받은 클래스의 인스턴스를 반환한다`() {
        val container = IocContainer()
        val actual = Injector(container).inject<TestRepository>()
        assertThat(container.getInstanceOrNull("TestRepository")).isNotNull()
    }

    @Test
    fun `ytfhf`() {
        val container = IocContainer()
        val actual = Injector(container).inject<TestRepository2>()
        assertThat(container.getInstanceOrNull("testDataSource")).isNotNull()
        assertThat(container.getInstanceOrNull("TestRepository2")).isNotNull()
    }
}