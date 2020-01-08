package com.wlm.mvvm_wanandroid.base


open class BaseRepository {
    suspend fun <T : Any> apiCall(call: suspend () -> HttpResponse<T>): HttpResponse<T> {
        return call.invoke()
    }

//    suspend fun <T: Any> saveApiCall(call: suspend () -> RequestResult<T>, errorMessage: String): RequestResult<T> {
//        return try {
//            call()
//        }catch (e: Exception) {
//            RequestResult.Error(IOException(errorMessage, e))
//        }
//    }
}