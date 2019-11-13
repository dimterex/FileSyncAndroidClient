package net.dimterex.sync_client.presenter.error

interface ErrorHandler {

    fun handle(error: Throwable)

    class Impl : ErrorHandler {

        override fun handle(error: Throwable) {
            error.printStackTrace()
        }
    }
}