package com.epro.fastdevtem.activity.login


import android.util.Log
import android.view.View
import com.epro.fastdevtem.R
import com.epro.fastdevtem.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {
    override fun showData(result: String) {
    }

    override val layoutId: Int = R.layout.activity_login

    override fun init() {
        registerClickEvent(registerClick,btn_login)

    }

    val registerClick:(view:View) ->Unit = {
        when (it.id) {
            R.id.btn_login -> {
                Log.e("LoginActivity","开始加载数据")
//                toast("开始加载数据")
                showLoadingDialog()
                mPresenter?.loadData()
            }
        }
    }

}
