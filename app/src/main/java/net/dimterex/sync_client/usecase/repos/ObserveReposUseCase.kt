package net.dimterex.sync_client.usecase.repos

//interface ObserveReposUseCase : ObservableUseCase<List<Repo>> {
//
//    class Impl(private val repoSource: ISettingsSource) : ObserveReposUseCase {
//
//        override suspend fun executeAsync(): Deferred<LiveData<List<Repo>>> =
//            CoroutineScope(Dispatchers.IO).async {
//                repoSource.observeAll()
//            }
//    }
//}