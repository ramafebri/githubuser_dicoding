package com.example.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchGituser(

	@field:SerializedName("total_count")
	val totalCount: Int?,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean?,

	@field:SerializedName("items")
	val items: List<ListGituser>
) : Parcelable


