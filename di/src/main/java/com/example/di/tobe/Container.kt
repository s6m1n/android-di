package com.example.di.tobe

interface Container {
    fun addModule(module: Any)

    fun addInstance(
        name: String,
        component: Any,
    )

    fun getInstanceOrNull(name: String): Any?
}
