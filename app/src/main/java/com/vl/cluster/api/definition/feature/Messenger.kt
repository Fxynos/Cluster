package com.vl.cluster.api.definition.feature

import com.vl.cluster.api.definition.entity.ChatDialog
import com.vl.cluster.api.definition.entity.PrivateDialog
import com.vl.cluster.api.definition.entity.ChatMessage
import com.vl.cluster.api.definition.entity.Dialog
import com.vl.cluster.api.definition.entity.PrivateMessage
import com.vl.cluster.api.definition.entity.Page

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