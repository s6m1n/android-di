package com.example.di.tobe

import com.example.di.InstanceProvideModule
import com.example.di.Provides
import com.example.di.TestDao
import com.example.di.TestDaoImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@InstanceProvideModule
object TestModule {
    @Provides
    fun testDao(): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModule2 {
    @Provides
    fun testDao1(): TestDao = TestDaoImpl()

    @Provides
    fun testDao2(): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModule3 {
    @Provides
    fun testDao1(): TestDao = TestDaoImpl()

    @Provides
    fun testDao2(testDao1: TestDao): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModule4 {
    @Provides
    fun testDao1(): TestDao = TestDaoImpl()

    @Provides
    fun testDao2(testDao1: TestDao): TestDao = TestDaoImpl()

    @Provides
    fun testDao3(
        testDao1: TestDao,
        testDao2: TestDao,
    ): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModule5 {
    @Provides
    fun testDao1(testDao3: TestDao): TestDao = TestDaoImpl()

    @Provides
    fun testDao2(
        testDao1: TestDao,
        testDao3: TestDao,
    ): TestDao = TestDaoImpl()

    @Provides
    fun testDao3(): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModuleA {
    @Provides
    fun testDao1(testDao2: TestDao): TestDao = TestDaoImpl()
}

@InstanceProvideModule
object TestModuleB {
    @Provides
    fun testDao2(): TestDao = TestDaoImpl()
}

class IocContainerTest {
    @Test
    fun test1() {
        val container = IocContainer()
        container.addModule(TestModule)
        assertThat(container.components.values).hasSize(1)
    }

    @Test
    fun test2() {
        val container = IocContainer()
        container.addModule(TestModule2)
        assertThat(container.components.values).hasSize(2)
    }

    @Test
    fun test3() {
        val container = IocContainer()
        container.addModule(TestModule3)
        assertThat(container.components.values).hasSize(2)
    }

    @Test
    fun test4() {
        val container = IocContainer()
        container.addModule(TestModule4)
        assertThat(container.components.values).hasSize(3)
    }

    @Test
    fun test5() {
        val container = IocContainer()
        container.addModule(TestModule5)
        assertThat(container.components.values).hasSize(3)
    }

    @Test(expected = NoSuchElementException::class)
    fun test6() {
        val container = IocContainer()
        container.addModule(TestModuleA)
        container.addModule(TestModuleB)
    }

    @Test
    fun test7() {
        val container = IocContainer()
        container.addModule(TestModuleB)
        container.addModule(TestModuleA)
        assertThat(container.components.values).hasSize(2)
    }
}
