package com.arctouch.codechallenge.data

enum class Status {
    RUNNING,
    SUCCESS,
    FAIL
}

data class RequestStatus private constructor(val status: Status, val message: String? = null) {
    companion object {
        val LOADED = RequestStatus(Status.SUCCESS)
        val LOADING = RequestStatus(Status.RUNNING)
        fun error(msg: String?) = RequestStatus(Status.FAIL, msg)

    }
}