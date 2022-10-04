package id.syaafiqi.photo_gallery_app.services

import android.annotation.SuppressLint
import id.syaafiqi.photo_gallery_app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkProvider {

    private val interceptor = HttpLoggingInterceptor()

    @SuppressLint("CustomX509TrustManager")
    val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(
                certs: Array<X509Certificate>,
                authType: String
            ) =
                Unit

            override fun checkServerTrusted(
                certs: Array<X509Certificate>,
                authType: String
            ) =
                Unit
        })

    private fun createSocketFactory(protocols: List<String>) =
        SSLContext.getInstance(protocols[0]).apply {
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

    private fun tokenInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val builder = request.newBuilder()
            BuildConfig.API_KEY.let { key ->
                if (key.isNotEmpty()) {
                    builder.addHeader(
                        "Authorization",
                        "Client-ID $key"
                    )
                }
            }

            chain.proceed(builder.build())
        }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .apply { this.addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)) }
        .sslSocketFactory(createSocketFactory(listOf("TLSv1.2")), trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
        .addInterceptor(tokenInterceptor())
        .build()


    val retrofitInstance: Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}