package com.example.di

import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class IocContainer {
    val components: MutableMap<String, Any> = mutableMapOf()

    fun addModule(module: Any) {
        val candidateFunctions = candidateComponents(module)
        addNoParametersInstances(candidateFunctions, module)
        addInstances(candidateFunctions.notYetComponent(), module)
    }

    private fun candidateComponents(module: Any): List<KFunction<*>> {
        return module::class.functions
            .filter { it.hasAnnotation<Provides>() }
    }

    private fun addNoParametersInstances(
        candidateFunctions: List<KFunction<*>>,
        module: Any,
    ) {
        candidateFunctions.filter { it.parameters.size == 1 }
            .map { addInstance(it.name, it.call(module)!!) }
    }

    private tailrec fun addInstances(
        candidateFunctions: List<KFunction<*>>,
        module: Any,
    ) {
        if (candidateFunctions.allComponents()) return
        val targetFunction = candidateFunctions.firstTargetFunction()
        val sourceComponents = targetFunction.findSourceComponents()
        addInstance(targetFunction.name, createInstance(targetFunction, module, sourceComponents))
        addInstances(candidateFunctions.notYetComponent(), module)
    }

    private fun List<KFunction<*>>.allComponents(): Boolean = notYetComponent().isEmpty()

    private fun List<KFunction<*>>.notYetComponent(): List<KFunction<*>> = filterNot { it.name in components }

    private fun List<KFunction<*>>.firstTargetFunction(): KFunction<*> = sortedBy { it.parameters.size }.first { it.canComponent() }

    private fun KFunction<*>.canComponent(): Boolean {
        return parameters
            .mapNotNull { it.name }
            .all { it in components }
    }

    private fun KFunction<*>.findSourceComponents(): List<Any> = parameters.drop(1).mapNotNull { components[it.name] }

    private fun createInstance(
        targetFunction: KFunction<*>,
        module: Any,
        sourceComponents: List<Any>,
    ): Any {
        return targetFunction.call(*(listOf(module) + sourceComponents).toTypedArray())!!
    }

    fun addInstance(
        name: String,
        component: Any,
    ) {
        if (components.contains(name)) throw RuntimeException("이미 등록된 컴포넌트입니다. ($name)")
        println("add $name from ${component::class.qualifiedName}")
        components[name] = component
    }

    fun getInstanceOrNull(name: String): Any? {
        return components[name]
    }
}
