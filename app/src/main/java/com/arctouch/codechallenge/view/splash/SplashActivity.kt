package com.arctouch.codechallenge.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.view.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {
    private val TIME_OUT = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        runSplash()
//        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                Cache.cacheGenres(it.genres)
//                startActivity(Intent(this, HomeActivity::class.java))
//                finish()
//            }
    }

    fun runSplash() {
        Handler().postDelayed({
            val it = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(it)
            finish()
        }, TIME_OUT.toLong())
    }
}
