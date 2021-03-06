package com.epro.fastdevtem.activity.login


import android.util.Log
import android.view.View
import com.epro.fastdevtem.R
import com.epro.fastdevtem.base.BaseActivity
import com.epro.fastdevtem.util.JsonFormat
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {
    override fun showData(result: String) {
        toast("加载数据完成")
        Logger.e("result = \n" + JsonFormat.format(result))
    }

    override val layoutId: Int = R.layout.activity_login

    override fun init() {
        registerClickEvent(registerClick,btn_login)
    }

    val registerClick:(view:View) ->Unit = {
        when (it.id) {
            R.id.btn_login -> {
                Log.e("LoginActivity","开始加载数据")
                toast("asdf",it,R.anim.shake)
                mPresenter?.loadData()

            }
        }
    }

}
