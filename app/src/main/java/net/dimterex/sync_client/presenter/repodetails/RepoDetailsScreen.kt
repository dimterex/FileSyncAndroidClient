package net.dimterex.sync_client.presenter.repodetails

import android.content.res.Resources
import android.os.Bundle
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.WrongDataError
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView

class EventDetailsPresenter(private val view: EventDetailsView, private val resources: Resources) : BasePresenter(view) {

//    private val observeRepoByIdUseCase by instance<ObserveRepoByIdUseCase>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        val id = arguments
            ?.getLong(resources.getString(R.string.key_repo_id), 0L) ?: 0L

        if (arguments != null && id  > 0) {
//            executor.executeParamsObservable(observeRepoByIdUseCase, id) {
//                it.await().observe(view, Observer {repo ->
//                    view.repo = repo
//                })
//            }
        } else {
        }
    }

    fun onBackButtonPressed() {
        view.back()
    }
}

interface EventDetailsView : BaseView {

    var repo: FileSyncState?

    fun back()
}