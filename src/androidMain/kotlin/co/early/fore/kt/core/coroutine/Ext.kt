package co.early.fore.kt.core.coroutine

import co.early.fore.kt.core.WorkMode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


/**
 * Would love to drop workMode entirely when using co-routines and just depend on
 * kotlinx-coroutines-test, sadly we can't always get determinate test results at
 * the moment - and for unit tests that's a total deal breaker.
 *
 * There is a complicated discussion about that here: https://github.com/Kotlin/kotlinx.coroutines/pull/1206
 *
 * In any case https://github.com/Kotlin/kotlinx.coroutines/blob/coroutines-test/kotlinx-coroutines-test/README.md
 * is focussed on swapping out the main dispatcher for unit tests. Even with runBlockingTest, other dispatchers can
 * still run concurrently with your tests, making tests much more complicated. The whole thing is extremely
 * complicated in fact (which is why it doesn't work yet).
 *
 * For the moment we continue to use the very simple and clear WorkMode switch as we have done in the past.
 * SYNCHRONOUS means everything is run sequentially in a blocking manner and on whatever thread the caller
 * is on. ASYNCHRONOUS gives you the co-routine behaviour you would expect.
 *
 * NB. This means there is no virtual time unless you implement it yourself though, for instance if you have code
 * like this in your app: delay(10 000), it will sit there and wait during a unit test, same as it would in app code.
 * You can write something like this instead: delay(if (workMode == WorkMode.ASYNCHRONOUS) 10000 else 1)
 *
 * Copyright © 2019 early.co. All rights reserved.
 */

fun launchMainImm(workMode: WorkMode, block: suspend CoroutineScope.() -> Unit): Job {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Main.immediate).launch { block() }
    }
}

fun launchMain(workMode: WorkMode, block: suspend CoroutineScope.() -> Unit): Job {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Main).launch { block() }
    }
}

fun launchIO(workMode: WorkMode, block: suspend CoroutineScope.() -> Unit): Job {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.IO).launch { block() }
    }
}

fun launchDefault(workMode: WorkMode, block: suspend CoroutineScope.() -> Unit): Job {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Default).launch { block() }
    }
}


fun <T> asyncMainImm(workMode: WorkMode, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Main.immediate).async { block() }
    }
}

fun <T> asyncMain(workMode: WorkMode, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Main).async { block() }
    }
}

fun <T> asyncIO(workMode: WorkMode, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.IO).async { block() }
    }
}

fun <T> asyncDefault(workMode: WorkMode, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        runBlocking { CompletableDeferred(block()) }
    } else {
        CoroutineScope(Dispatchers.Default).async { block() }
    }
}

suspend fun <T> withContextMainImm(workMode: WorkMode, block: suspend CoroutineScope.() -> T): T {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        block(CoroutineScope(coroutineContext))
    } else {
        withContext(Dispatchers.Main.immediate) { block() }
    }
}

suspend fun <T> withContextMain(workMode: WorkMode, block: suspend CoroutineScope.() -> T): T {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        block(CoroutineScope(coroutineContext))
    } else {
        withContext(Dispatchers.Main) { block() }
    }
}

suspend fun <T> withContextIO(workMode: WorkMode, block: suspend CoroutineScope.() -> T): T {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        block(CoroutineScope(coroutineContext))
    } else {
        withContext(Dispatchers.IO) { block() }
    }
}

suspend fun <T> withContextDefault(workMode: WorkMode, block: suspend CoroutineScope.() -> T): T {
    return if (workMode == WorkMode.SYNCHRONOUS) {
        block(CoroutineScope(coroutineContext))
    } else {
        withContext(Dispatchers.Default) { block() }
    }
}
