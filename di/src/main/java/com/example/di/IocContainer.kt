package com.example.di

import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class IocContainer {
    val components: MutableMap<String, Any> = mutableMapOf()

    fun addModule(module: Any) {
        val functions = module::class.functions.filter { it.hasAnnotation<Provides>() }
        if (functions.size == components.size) {
            return
        }
        val target = functions
            .filterNot { components.contains(it.name) }
            .sortedBy { it.parameters.size }
            .first {
                it.parameters.size == 1 || (
                        it.parameters
                            .mapNotNull { parameter -> parameter.name }
                            .all { name -> components.contains(name) }
                        )
            }
        val instances = target.parameters
            .drop(1)
            .map { components[it.name] }
        val instance = if (instances.isEmpty()) {
            target.call(module)
        } else {
            target.call(*(listOf(module) + instances).toTypedArray())
        }
        addInstance(target.name, instance as Any)
        return addModule(module)
    }

    fun addInstance(name: String, component: Any) {
        println("$name to ${component::class.qualifiedName}")
        if (components.contains(name)) throw RuntimeException("이미 등록된 컴포넌트입니다.")
        components[name] = component
    }

    fun getInstanceOrNull(name: String): Any? {
        return components[name]
    }
}