package com.example.dicodingstory.data.remote.retrofit

import androidx.lifecycle.MutableLiveData
import com.example.dicodingstory.data.remote.response.ListStoryItem
import com.example.dicodingstory.data.remote.response.LoginResult
import com.example.dicodingstory.data.remote.response.StoryErrorResponse
import com.example.dicodingstory.data.remote.response.StoryListResponse
import com.example.dicodingstory.data.remote.response.StoryLoginResponse
import com.example.dicodingstory.utils.isValidEmail
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    override suspend fun registerAccount(
        name: String,
        email: String,
        password: String
    ): StoryErrorResponse {
        return if (name.isEmpty()) {
            StoryErrorResponse(
                error = true,
                message = "\"name\" is not allowed to be empty"
            )
        } else if (email.isEmpty()) {
            StoryErrorResponse(
                error = true,
                message = "\"email\" is not allowed to be empty"
            )
        } else if (!isValidEmail(email)) {
            StoryErrorResponse(
                error = true,
                message = "\"email\" must be a valid email"
            )
        } else if (password.isEmpty()) {
            StoryErrorResponse(
                error = true,
                message = "\"password\" is not allowed to be empty"
            )
        } else if (password.length < 8) {
            StoryErrorResponse(
                error = true,
                message = "Password must be at least 8 characters long"
            )
        } else {
            StoryErrorResponse(
                error = false,
                message = "Registration successful"
            )
        }
    }

    override suspend fun loginAccount(
        email: String,
        password: String
    ): StoryLoginResponse {
        return if (email.isEmpty()) {
            StoryLoginResponse(
                error = true,
                message = "\"email\" is not allowed to be empty"
            )
        } else if (!isValidEmail(email)) {
            StoryLoginResponse(
                error = true,
                message = "\"email\" must be a valid email"
            )
        } else if (password.isEmpty()) {
            StoryLoginResponse(
                error = true,
                message = "\"password\" is not allowed to be empty"
            )
        } else if (password.length < 8) {
            StoryLoginResponse(
                error = true,
                message = "make sure your password is at least 8 characters"
            )
        } else {
            StoryLoginResponse(
                loginResult = LoginResult(
                    userId = "user-id",
                    name = "name",
                    token = "token"
                ),
                error = false,
                message = "Login successful"
            )
        }
    }

    override suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): StoryErrorResponse {
        return if (description.toString().isEmpty()) {
            StoryErrorResponse(
                error = true,
                message = "description is required"
            )
        } else {
            StoryErrorResponse(
                error = false,
                message = "Story created successfully"
            )
        }
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Boolean
    ): StoryListResponse {
        val items: StoryListResponse
        val storyList: MutableList<ListStoryItem> = arrayListOf()

        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i",
                i.toDouble(),
                i.toDouble()
            )
            storyList.add(story)
        }

        if (page != null && size != null) {
            storyList.subList((page - 1) * size, (page - 1) * size + size)
        }

        items = StoryListResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = storyList
        )

        return items
    }
}
