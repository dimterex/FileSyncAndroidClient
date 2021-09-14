package net.dimterex.sync_client.presenter.menu.sync

import android.os.Bundle
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.modules.ExecuteManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class SyncPresenter(private val view: SyncView) : BasePresenter(view) {

    private val _executerManager by instance<ExecuteManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
    }

    fun sync_execute() {

//        coroutineScope.launch {
//            _file_executor.download()
//        }

        _executerManager.execute(SyncFilesRequest())
//        runBlocking {
//            _file_executor.download()
//        }
    }
}

interface SyncView : BaseView {
}