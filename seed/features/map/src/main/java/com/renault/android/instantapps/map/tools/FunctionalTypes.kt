package com.renault.android.instantapps.map.tools

open class Result<in T> private constructor() {

    companion object {
        fun <TIn> success(value: TIn) : Success<TIn> = Success(value)
        fun <TIn> failure() : Failure<TIn> = Failure()
    }

    class Success<T>(val value: T) : Result<T>()
    class Failure<T> : Result<T>()
}

infix fun <TIn, TInter, TOut> ((TIn) -> Result<TInter>).flow(g: (TInter) -> Result<TOut>): (TIn) -> Result<TOut> {
    val f = this
    return { x: TIn ->
        val result = f(x)
        when (result) {
            is Result.Success -> { g(result.value) }
            else -> {
                Result.Failure<TOut>()
            }
        }
    }
}