package dev.wnuke.nukeclient.event

import java.lang.reflect.Method
import kotlin.reflect.KClass


class EventManager {
    private val registeredMethods: HashMap<Pair<Any, Method>, Class<*>> = HashMap()

    fun register(clazz: Any) {
        clazz.javaClass.declaredMethods.forEach {
            if (it.isAnnotationPresent(Subscribe::class.java)) {
                if (it.parameters.size == 1) {
                    if (it.parameters[0].type.genericSuperclass == Event::class.java) {
                        registeredMethods[Pair(clazz, it)] = it.parameters[0].type
                    }
                }
            }
        }
    }

    fun unRegister(clazz: Any) {
        clazz.javaClass.declaredMethods.forEach {
            if (it.isAnnotationPresent(Subscribe::class.java)) {
                if (it.parameters.size == 1) {
                    if (it.parameters[0].type.genericSuperclass == Event::class.java) {
                        registeredMethods.remove(Pair(clazz, it))
                    }
                }
            }
        }
    }

    fun post(event: Any) {
        registeredMethods.forEach { (t, u) -> if (u == event.javaClass) t.second.invoke(t.first, event) }
    }
}