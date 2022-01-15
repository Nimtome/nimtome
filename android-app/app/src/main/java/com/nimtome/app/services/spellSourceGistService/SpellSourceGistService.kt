package com.nimtome.app.services.spellSourceGistService

import com.nimtome.app.services.spellSourceGistService.models.SpellSourcesGist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.*

class SpellSourceGistService {

    private val gistService: GistApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com")
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        this.gistService = retrofit.create(GistApi::class.java)
    }

    private interface GistApi {
        @GET("/{userName}/{gistId}/raw")
        fun getSources(
            @Path("userName") userName: String,
            @Path("gistId") gistId: String,
            @Query("cacheBuster") cacheBuster: String
        ): Call<SpellSourcesGist>?

        @GET("/{userName}/{gistId}/raw")
        fun getSpellsFromSource(
            @Path("userName") userName: String,
            @Path("gistId") gistId: String,
            @Query("cacheBuster") cacheBuster: String
        ): Call<SpellSourcesGist>?
    }

    fun fetchSources(
        userName: String,
        gistId: String,
        onResponse: (sources: SpellSourcesGist?) -> Unit
    ) {
        val timestamp: Long = Calendar.getInstance().timeInMillis / 1000

        this.gistService.getSources(userName, gistId, "_$timestamp")
            ?.enqueue(object : Callback<SpellSourcesGist> {
                override fun onResponse(
                    call: Call<SpellSourcesGist>,
                    response: Response<SpellSourcesGist>
                ) {
                    onResponse(response.body())
                }

                override fun onFailure(call: Call<SpellSourcesGist>, t: Throwable) {
                    onResponse(null)
                }
            })
    }

    fun fetchSpellsFromSource() {
        
    }
}