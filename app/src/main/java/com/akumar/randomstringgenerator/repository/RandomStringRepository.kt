package com.akumar.randomstringgenerator.repository

import android.content.ContentResolver
import android.os.Bundle
import androidx.core.net.toUri
import com.akumar.randomstringgenerator.data.model.RandomStringItem
import com.akumar.randomstringgenerator.ui.screens.RandomStringFetchResult
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RandomStringRepository(private val contentResolver: ContentResolver):IRandomStringRepository {
    private val DATA_URI = "content://com.iav.contestdataprovider/text"
    private val DATA_COLUMN_NAME = "data"

     override fun getRandomString(randomStringLength: Int): RandomStringFetchResult {
        val uri = DATA_URI.toUri()
        val projection = arrayOf(DATA_COLUMN_NAME)
        val queryArgs = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, randomStringLength)
        }

        try {
            contentResolver.query(uri, projection, queryArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val jsonStr = cursor.getString(cursor.getColumnIndexOrThrow(DATA_COLUMN_NAME))
                    val json = JSONObject(jsonStr).getJSONObject("randomText")

                    val formattedCreatedDate = formatDate(json.getString("created"))

                    return RandomStringFetchResult.Success(
                        RandomStringItem(
                            value = json.getString("value"),
                            length = json.getInt("length"),
                            created = formattedCreatedDate
                        )
                    )
                } else {
                    return RandomStringFetchResult.Error("No data found")
                }
            } ?: run {
                return RandomStringFetchResult.Error("Something went wrong. Please try again!!")
            }
        } catch (e: SecurityException) {
            return RandomStringFetchResult.Error("You do not have permission to view this content")
        } catch (e: Exception) {
            return RandomStringFetchResult.Error("Something went wrong. Please try again!!")
        }
    }


    private fun formatDate(dateStr: String): String {
        return try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

            val date = inputFormatter.parse(dateStr) ?: return ""

            val outputFormatter = SimpleDateFormat("dd MMMM, yyyy HH:mm", Locale.getDefault())
            outputFormatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}