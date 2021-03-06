package com.wesleydonk.update.ui.internal.extensions

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
internal open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
internal class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { t ->
            onEventUnhandledContent(t)
        }
    }
}

@MainThread
internal inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<Event<T>> {
    val wrappedObserver = EventObserver<T> { t ->
        onChanged.invoke(t)
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

@MainThread
internal inline fun <T> LiveData<Event<T>>.observeEventForever(
    crossinline onChanged: (T) -> Unit
): Observer<Event<T>> {
    val wrappedObserver = EventObserver<T> { t ->
        onChanged.invoke(t)
    }
    observeForever(wrappedObserver)
    return wrappedObserver
}
