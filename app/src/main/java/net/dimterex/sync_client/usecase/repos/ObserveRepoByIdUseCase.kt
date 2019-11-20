package net.dimterex.sync_client.usecase.repos

//interface ObserveRepoByIdUseCase : ObservableParamsUseCase<Repo, Long> {
//
//    class Impl(private val repoSource: ISettingsSource) : ObserveRepoByIdUseCase {
//
//        override suspend fun executeAsync(params: Long): Deferred<LiveData<Repo>> =
////            CoroutineScope(Dispatchers.IO).async {
////                repoSource.observeById(params)
////            }
//    }
//}