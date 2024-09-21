package com.example.di.tobe

import com.example.di.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class Injector(val container: Container) {
    inline fun <reified T : Any> inject(): T {
        val parameters =
            T::class.primaryConstructor?.parameters ?: throw RuntimeException("주 생성자가 존재하지 않습니다.")
        val sourceComponents = parameters
            .filter { it.hasAnnotation<Inject>() }
            .map {
                if (!container.contains(it.name!!)) {
                    val instance = (it.type.classifier as KClass<*>).createInstance()
                    container.addInstance(it.name!!, instance)
                }
                container.getInstanceOrNull(it.name!!)!!
            }

        val instance = T::class.primaryConstructor!!.call(*sourceComponents.toTypedArray())
        container.addInstance(instance::class.simpleName!!, instance)
        return container.getInstanceOrNull(instance::class.simpleName!!) as T
    }
}