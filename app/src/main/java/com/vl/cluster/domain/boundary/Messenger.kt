package com.vl.cluster.domain.boundary

import com.vl.cluster.domain.entity.ChatDialog
import com.vl.cluster.domain.entity.PrivateDialog
import com.vl.cluster.domain.entity.ChatMessage
import com.vl.cluster.domain.entity.Dialog
import com.vl.cluster.domain.entity.PrivateMessage
import com.vl.cluster.domain.entity.Page

interface Messenger {
    fun getDialog(dialogId: Long): Dialog
    fun fetchDialogs(count: Int, key: String? = null): Page<String, Dialog>

    fun fetchMessages(
        dialog: PrivateDialog,
        count: Int, key: String? = null
    ): Page<String, PrivateMessage>

    fun fetchMessages(
        dialog: ChatDialog,
        count: Int, key: String? = null
    ): Page<String, ChatMessage>

    fun sendMessage(message: PrivateMessage): PrivateMessage
    fun sendMessage(message: ChatMessage): ChatMessage
}