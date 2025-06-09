package com.example.dicodingstory.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoryBasicResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
