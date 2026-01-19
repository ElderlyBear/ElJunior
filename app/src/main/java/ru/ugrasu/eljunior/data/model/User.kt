package ru.ugrasu.eljunior.data.model

import com.google.gson.annotations.SerializedName

/**
 * Moodle token response after successful login
 */
data class TokenResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("privatetoken") val privateToken: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("errorcode") val errorCode: String?
)

/**
 * User site info from Moodle
 */
data class SiteInfo(
    @SerializedName("userid") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstname") val firstName: String,
    @SerializedName("lastname") val lastName: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("sitename") val siteName: String,
    @SerializedName("userpictureurl") val avatarUrl: String?,
    @SerializedName("lang") val language: String?
)

/**
 * User profile data for UI
 */
data class UserProfile(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val avatarUrl: String?,
    val role: String = "Студент ЮГУ"
) {
    companion object {
        fun fromSiteInfo(siteInfo: SiteInfo): UserProfile {
            return UserProfile(
                id = siteInfo.userId,
                username = siteInfo.username,
                firstName = siteInfo.firstName,
                lastName = siteInfo.lastName,
                fullName = siteInfo.fullName,
                avatarUrl = siteInfo.avatarUrl
            )
        }
    }

    fun getInitials(): String {
        val first = firstName.firstOrNull()?.uppercase() ?: ""
        val last = lastName.firstOrNull()?.uppercase() ?: ""
        return "$first$last"
    }

    fun getShortName(): String {
        val lastInitial = lastName.firstOrNull()?.let { "$it." } ?: ""
        return "$firstName $lastInitial"
    }
}
