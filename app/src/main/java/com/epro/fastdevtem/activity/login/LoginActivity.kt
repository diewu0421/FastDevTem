package com.epro.fastdevtem.activity.login


import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.epro.fastdevtem.R
import com.epro.fastdevtem.base.BaseActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import chihane.jdaddressselector.BottomDialog
import chihane.jdaddressselector.model.Street
import chihane.jdaddressselector.model.County
import chihane.jdaddressselector.model.City
import chihane.jdaddressselector.model.Province
import chihane.jdaddressselector.AddressSelector
import chihane.jdaddressselector.OnAddressSelectedListener






class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {
    override fun showData(result: String) {
        runOnUiThread{
            hideLoadingDialog()
            toast("加载数据完成")
            Logger.e("加载数据完成\n$result")
        }
    }

    override val layoutId: Int = R.layout.activity_login

    override fun init() {
        registerClickEvent(registerClick,btn_login)
    }

    val registerClick:(view:View) ->Unit = {
        when (it.id) {
            R.id.btn_login -> {
//                toast("开始加载数据")
//                showLoadingDialog()
//                mPresenter.loadData()

//                val dialog = BottomDialog(context)
//                dialog.setOnAddressSelectedListener{province, city, county, street ->
//                    Logger.e("${province?.name} ${city?.name} ${county?.name} ${street?.name}")
//                }
//                dialog.show()

                val selector = AddressSelector(context)
                selector.onAddressSelectedListener = OnAddressSelectedListener { province, city, county, street ->
                    Logger.e("${province?.name} ${city?.name} ${county?.name} ${street?.name}")
                }

                val view = selector.view
// frameLayout.addView(view)
// new AlertDialog.Builder(context).setView(view).show()

                AlertDialog.Builder(context).setView(view).show()
            }
        }
    }
}
