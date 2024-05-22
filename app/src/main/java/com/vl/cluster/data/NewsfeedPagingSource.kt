package com.vl.cluster.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vl.cluster.domain.boundary.Newsfeed
import com.vl.cluster.domain.entity.Post

class NewsfeedPagingSource(private val newsfeed: Newsfeed): PagingSource<String, Post>() {
    override fun getRefreshKey(state: PagingState<String, Post>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            newsfeed.fetchNews(
                count = params.loadSize,
                key = params.key
            ).run { LoadResult.Page(items, null, nextKey) }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}