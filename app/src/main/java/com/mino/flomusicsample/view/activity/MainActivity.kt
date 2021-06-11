package com.mino.flomusicsample.view.activity

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.mino.flomusicsample.R
import com.mino.flomusicsample.base.BaseActivity
import com.mino.flomusicsample.databinding.ActivityMainBinding
import com.mino.flomusicsample.view.fragment.MusicPlayFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MusicPlayFragment>(R.id.fv_container)
            }
        }
    }
}