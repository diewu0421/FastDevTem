package com.epro.fastdevtem.widget

/**
 * VerifyCodeView
 * @author zlw
 * @date 2017/9/18
 */
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.epro.fastdevtem.R
import java.util.Random

class VerifyCode : View {

    private var mCodeText: String? = null//文本内容
    private var mCodeTextSize: Int = 0//文本大小
    private var mCodeLength: Int = 0//验证码长度
    private var mCodeBackground: Int = 0//背景色
    private var isContainChar: Boolean = false//验证码是否包含字母
    private var mPointNum: Int = 0//干扰点数
    private var mLineNum: Int = 0//干扰线数

    private var mPaint: Paint? = null//画笔
    private var mBound: Rect? = null//绘制范围
    private var bitmap: Bitmap? = null//验证码图片


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrValues(context, attrs)
        initData()
    }

    /**
     * 初始化属性集合
     * @param context
     * @param attrs
     */
    private fun initAttrValues(context: Context, attrs: AttributeSet) {
        // //获取在AttributeSet中定义的 VerifyCode 中声明的属性的集合
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyCode)
        //获取TypeArray的长度
        val count = typedArray.indexCount
        for (i in 0..count - 1) {
            //获取此项属性的ID
            val index = typedArray.getIndex(i)
            when (index) {
                R.styleable.VerifyCode_codeTextSize ->
                    // 默认设置为16sp，TypeValue类 px转sp 一个转换类
                    mCodeTextSize = typedArray.getDimensionPixelSize(index, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics).toInt())
                R.styleable.VerifyCode_codeBackground -> mCodeBackground = typedArray.getColor(index, Color.WHITE)
                R.styleable.VerifyCode_codeLength -> mCodeLength = typedArray.getInteger(index, 4)
                R.styleable.VerifyCode_isContainChar -> isContainChar = typedArray.getBoolean(index, false)
                R.styleable.VerifyCode_pointNum -> mPointNum = typedArray.getInteger(index, 100)
                R.styleable.VerifyCode_linNum -> mLineNum = typedArray.getInteger(index, 3)
            }
        }
        typedArray.recycle()
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        mCodeText = getValidationCode(mCodeLength, isContainChar)
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mBound = Rect()
        //计算文字所在矩形，可以得到宽高
        mPaint!!.getTextBounds(mCodeText, 0, mCodeText!!.length, mBound)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取控件宽高的显示模式
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        //获取宽高的尺寸值  固定值的宽度
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //设置宽高默认为建议的最小宽高
        var width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        var height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        //        MeasureSpec父布局传递给后代的布局要求 包含 确定大小和三种模式
        //        EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
        //        AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
        //        UNSPECIFIED：表示子布局想要多大就多大，很少使用
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            mPaint!!.textSize = mCodeTextSize.toFloat()
            mPaint!!.getTextBounds(mCodeText, 0, mCodeText!!.length, mBound)
            val textWidth = mBound!!.width().toFloat()
            val tempWidth = (paddingLeft.toFloat() + textWidth + paddingRight.toFloat()).toInt()
            width = tempWidth
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            mPaint!!.textSize = mCodeTextSize.toFloat()
            mPaint!!.getTextBounds(mCodeText, 0, mCodeText!!.length, mBound)
            val textHeight = mBound!!.height().toFloat()
            val tempHeight = (paddingTop.toFloat() + textHeight + paddingBottom.toFloat()).toInt()
            height = tempHeight
        }
        //设置测量的宽高
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mWidth = width
        mHeight = height

        if (bitmap == null) {
            bitmap = createBitmapValidate()
        }
        canvas.drawBitmap(bitmap!!, 0f, 0f, mPaint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> refresh()
        }
        return super.onTouchEvent(event)
    }

    /**
     * 创建图片验证码
     * @return
     */
    private fun createBitmapValidate(): Bitmap {
        if (bitmap != null && !bitmap!!.isRecycled) {
            //回收并且置为null
            bitmap!!.recycle()
            bitmap = null
        }
        //创建图片
        val sourceBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        //创建画布
        val canvas = Canvas(sourceBitmap)
        //画上背景颜色
        canvas.drawColor(mCodeBackground)
        //初始化文字画笔
        mPaint!!.strokeWidth = 3f
        mPaint!!.textSize = mCodeTextSize.toFloat()
        //测量验证码字符串显示的宽度值
        val textWidth = mPaint!!.measureText(mCodeText)
        //画上验证码
        val length = mCodeText!!.length
        //计算一个字符的所占位置
        val charLength = textWidth / length
        for (i in 1..length) {
            var offsetDegree = mRandom.nextInt(15)
            //这里只会产生0和1，如果是1那么正旋转正角度，否则旋转负角度
            offsetDegree = if (mRandom.nextInt(2) == 1) offsetDegree else -offsetDegree
            //用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
            canvas.save()
            //设置旋转
            canvas.rotate(offsetDegree.toFloat(), (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
            //给画笔设置随机颜色
            mPaint!!.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
                    mRandom.nextInt(200) + 20)
            //设置字体的绘制位置
            canvas.drawText(mCodeText!![i - 1].toString(), (measuredWidth/2- mBound?.width()!!) + (i - 1) * charLength ,
                    mHeight * 4 / 5f, mPaint!!)

//            canvas.drawText(mCodeText!![i-1].toString(),
//                    (measuredWidth /2 - (mBound?.centerX()?.div(2) ?: 0)).toFloat(),
//                    (measuredHeight /2 + (mBound?.height()?.div(2) ?: 0)).toFloat(),
//                    mPaint)

            //用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
            canvas.restore()
        }

        //重新设置画笔
        mPaint!!.setARGB(255, mRandom.nextInt(200) + 20, mRandom.nextInt(200) + 20,
                mRandom.nextInt(200) + 20)
        mPaint!!.strokeWidth = 1f
        //产生干扰效果1 －－ 干扰点
        for (i in 0..mPointNum - 1) {
            drawPoint(canvas, mPaint)
        }
        //生成干扰效果2 －－ 干扰线
        for (i in 0..mLineNum - 1) {
            drawLine(canvas, mPaint)
        }
        return sourceBitmap
    }

    /**
     * 获取验证码
     *
     * @param length 生成随机数的长度
     * @param contains 是否包含字符串
     * @return
     */
    fun getValidationCode(length: Int, contains: Boolean): String {
        var `val` = ""
        val random = Random()

        for (i in 0..length - 1) {
            if (contains) {
                //字母或数字
                val code = if (random.nextInt(2) % 2 == 0) "char" else "num"
                //字符串
                if ("char".equals(code, ignoreCase = true)) {
                    //大写或小写字母
                    val choice = if (random.nextInt(2) % 2 == 0) 65 else 97
                    `val` += (choice + random.nextInt(26)).toChar()
                } else if ("num".equals(code, ignoreCase = true)) {
                    `val` += random.nextInt(10).toString()
                }
            } else {
                `val` += random.nextInt(10).toString()
            }

        }
        return `val`
    }

    /**
     * 判断验证码是否一致 忽略大小写
     */
    fun isEqualsIgnoreCase(CodeString: String): Boolean? {
        return mCodeText!!.equals(CodeString, ignoreCase = true)
    }

    /**
     * 判断验证码是否一致 不忽略大小写
     */
    fun isEquals(CodeString: String): Boolean? {
        return mCodeText == CodeString
    }

    /**
     * 提供外部调用的刷新方法
     */
    fun refresh() {
        mCodeText = getValidationCode(mCodeLength, isContainChar)
        bitmap = createBitmapValidate()
        invalidate()
    }

    companion object {

        private val mRandom = Random()
        private var mWidth: Int = 0//控件的宽度
        private var mHeight: Int = 0//控件的高度

        /**
         * 生成干扰点
         */
        private fun drawPoint(canvas: Canvas, paint: Paint?) {
            val pointF = PointF((mRandom.nextInt(mWidth) + 10).toFloat(), (mRandom.nextInt(mHeight) + 10).toFloat())
            canvas.drawPoint(pointF.x, pointF.y, paint)
        }

        /**
         * 生成干扰线
         */
        private fun drawLine(canvas: Canvas, paint: Paint?) {
            val startX = mRandom.nextInt(mWidth)
            val startY = mRandom.nextInt(mHeight)
            val endX = mRandom.nextInt(mWidth)
            val endY = mRandom.nextInt(mHeight)
            canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), paint)
        }
    }
}