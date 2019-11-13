package net.dimterex.sync_client.ui.listener

import androidx.recyclerview.widget.RecyclerView

class OnScrolldedToBottomListener(
    private val onScrolledToBottom: () -> Unit
) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)
            && newState == RecyclerView.SCROLL_STATE_IDLE) {
            onScrolledToBottom()
        }
    }
}