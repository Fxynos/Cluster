package com.vl.cluster.data.network.vk

import com.vk.api.sdk.objects.newsfeed.Filters
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse
import com.vk.api.sdk.objects.wall.WallpostAttachment
import com.vl.cluster.data.network.vk.VkNetwork.VkSession
import com.vl.cluster.domain.boundary.Newsfeed
import com.vl.cluster.domain.entity.Attachment
import com.vl.cluster.domain.entity.Comment
import com.vl.cluster.domain.entity.Group
import com.vl.cluster.domain.entity.Page
import com.vl.cluster.domain.entity.Post
import com.vl.cluster.domain.entity.Profile
import com.vl.cluster.domain.entity.User

class VkNewsfeed(private val session: VkSession): Newsfeed {

    private val user by session::user
    private val client by session::client

    override fun getPost(postId: Long): Post {
        TODO("Not yet implemented")
    }

    override fun getComment(commentId: Long): Comment {
        TODO("Not yet implemented")
    }

    override fun getProfile(profileId: Long): Profile {
        TODO("Not yet implemented")
    }

    override fun fetchNews(source: Profile?, count: Int, key: String?): Page<String, Post> =
        client.newsfeed()[user]
            .count(count)
            .filters(Filters.POST)
            .run { if (source == null) this else sourceIds(source.profileId) }
            .run { if (key == null) this else startFrom(key) }
            .execute()
            .run {
                Page(
                    items.map { raw ->
                        val post = raw.oneOf0!!
                        val isWallOwner = user.id == post.sourceId
                        val sourceProfile = getProfile(post.sourceId)

                        Post(
                            session,
                            post.postId.toString(),
                            sourceProfile,
                            if (post.signerId == null) sourceProfile else getProfile(post.signerId),
                            post.date.toLong(),
                            post.text,
                            post.attachments.mapNotNull { getAttachment(it) },
                            post.views.count.toLong(),
                            post.likes.count.toLong(),
                            post.comments.count.toLong(),
                            post.reposts.count.toLong(),
                            post.likes.canLike(),
                            post.comments.canPost(),
                            true,
                            isWallOwner,
                            isWallOwner,
                            post.likes.userLikes != 0
                        )
                    },
                    nextFrom
                )
            }

    override fun fetchComments(post: Post, count: Int, key: String?): Page<String, Comment> {
        TODO("Not yet implemented")
    }

    override fun setLike(post: Post): Post {
        TODO("Not yet implemented")
    }

    override fun leaveComment(comment: Comment): Comment {
        TODO("Not yet implemented")
    }

    /* Utils */

    private fun GetResponse.getProfile(id: Int) =
        if (id < 0)
            groups.find { it.id == -id }!!.run {
                Group(
                    session.network, id.toString(), name,
                    photo400?.toString() ?: photo200?.toString() ?: photo100?.toString() ?: photo50?.toString()
                )
            }
        else
            profiles.find { it.id == id }!!.run {
                User(session.network, id.toString(), firstNameAbl, lastNameAbl, photo)
            }

    private fun GetResponse.getAttachment(attachment: WallpostAttachment): Attachment? =
        attachment.photo?.run {
            Attachment.Image(
                session,
                sizes.maxBy { it.width * it.height }.url.toString(),
                getProfile(ownerId)
            )
        }
}