package com.vl.cluster.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.vl.cluster.data.NewsfeedPagingSource
import com.vl.cluster.data.getDatetime
import com.vl.cluster.data.network.vk.VkNetwork
import com.vl.cluster.domain.entity.Attachment
import com.vl.cluster.domain.manager.AuthManager
import com.vl.cluster.presentation.entity.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    app: Application,
    vk: VkNetwork,
    authManager: AuthManager
): AndroidViewModel(app) {
    private val context: Context get() = getApplication()
    private val _newsfeed = Pager(PagingConfig(50)) {
        // TODO [tva] aggregate from all sessions
        NewsfeedPagingSource(authManager.getSessions(vk.networkId).first().newsfeed)
    }.flow.cachedIn(viewModelScope)

    val newsfeed = _newsfeed.map { pagingData -> pagingData.map { post ->
        Post(
            id = post.postId,
            title = post.source.name,
            datetime = getDatetime(context, post.unixSec),
            text = post.text,
            profileImage = post.source.imageUrl,
            networkIcon = post.session.network.icon,
            images = post.attachments.mapNotNull { (it as? Attachment.Image)?.resourceUrl },
            likesCount = post.likesCount,
            commentsCount = post.commentsCount,
            repostsCount = post.repostsCount,
            hasLike = post.hasLike
        )
    } }
}