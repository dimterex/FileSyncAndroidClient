package net.dimterex.sync_client.usecase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred

interface UseCase<T> {

    suspend fun executeAsync(): Deferred<T>
}

interface ObservableParamsUseCase<TResult, TParams> {

    suspend fun executeAsync(params: TParams): Deferred<LiveData<TResult>>
}

interface ObservableUseCase<TResult> {

    suspend fun executeAsync(): Deferred<LiveData<TResult>>
}