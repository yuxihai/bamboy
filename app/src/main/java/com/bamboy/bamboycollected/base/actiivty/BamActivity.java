package com.bamboy.bamboycollected.base.actiivty;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bamboy.bamboycollected.R;
import com.bamboy.bamboycollected.page.anim_click.AnimClickActivity;
import com.bamboy.bamboycollected.page.auto_line.AutoLineActivity;
import com.bamboy.bamboycollected.page.blur.BlurActivity;
import com.bamboy.bamboycollected.page.divide_load.DivideLoadActivity;
import com.bamboy.bamboycollected.page.freedom.FreedomListActivity;
import com.bamboy.bamboycollected.page.main.MainActivity;
import com.bamboy.bamboycollected.page.noun_progress.NounProgressActivity;
import com.bamboy.bamboycollected.page.toast.ToastActivity;
import com.bamboy.bamboycollected.utils.UtilBox;

/**
 * Activity 基类
 * <p/>
 * 右滑关闭
 * 沉浸式
 * 先显示后加载
 * 工具箱
 * <p/>
 * Created by Bamboy on 2017/3/24.
 */
public abstract class BamActivity extends Activity {

    /**
     * 触摸工具类
     */
    private UtilBaseGesture mUtilGesture;
    /**
     * 必备工具类
     */
    private BaseWantUtil mWantUtil;

    /**
     * 执行模拟onCreate标记
     */
    protected boolean isCreate = true;
    /**
     * 工具箱
     */
    public UtilBox utils = UtilBox.getBox();


    /**
     * 标题栏
     */
    protected RelativeLayout rl_title;
    /**
     * 标题栏关闭箭头
     */
    protected ImageView iv_back;
    /**
     * 标题栏文字
     */
    protected TextView tv_title;
    /**
     * 标题栏介绍Icon
     */
    protected ImageView iv_introduce;
    /**
     * 关闭介绍Icon
     */
    protected ImageView iv_introduce_close;
    /**
     * 介绍容器
     */
    protected RelativeLayout rl_introduce;
    /**
     * 介绍内容
     */
    protected TextView tv_introduce;

    /**
     * Activity的Layout的根View
     * 【滑动过程中会移动的View】
     */
    private View moveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isCreate = true;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // 手势工具类
        mUtilGesture = new UtilBaseGesture(this);
        // 必备工具类
        mWantUtil = new BaseWantUtil(this);

        /**
         * 如果是Android 4.4 以上，就兼容沉浸式
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                // 初始化状态栏
                mWantUtil.initBar();
            } catch (Exception e) {
                utils.want.showException(e);
            }
        }
        initView();
    }

    /**
     * 初始化View
     * 状态栏 及 介绍页
     * 这些View是本App专用
     * 写Demo的时候无须搭理此方法
     */
    private void initView() {
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_introduce = (ImageView) findViewById(R.id.iv_introduce);
        iv_introduce_close = (ImageView) findViewById(R.id.iv_introduce_close);
        rl_introduce = (RelativeLayout) findViewById(R.id.rl_introduce);
        tv_introduce = (TextView) findViewById(R.id.tv_introduce);

        // 设置titleBar
        setImmerseTitleBar(rl_title);
        // 处理View内容
        initViewContent();
        // 处理View点击事件
        initViewClick();

        if (iv_introduce != null && iv_introduce_close != null) {
            iv_introduce.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) iv_introduce_close.getLayoutParams();

                    int height = utils.ui.getBarHeight(BamActivity.this);
                    params.setMargins(-1, height, -1, -1);
                }
            });
        }

        // 设置只移动TitleBar以下的内容
        moveView = findViewById(R.id.rl_root);
        setMoveView(moveView);
    }

    /**
     * 处理View内容
     */
    private void initViewContent() {
        if (this instanceof MainActivity) {
            iv_back.setVisibility(View.GONE);
            tv_title.setText("Bamboy合集");
            tv_introduce.setText(getString(R.string.introduce_main));

        } else if (this instanceof ToastActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("Toast Demo");
            tv_introduce.setText(getString(R.string.introduce_toast));

        } else if (this instanceof BlurActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("高斯模糊 Demo");
            tv_introduce.setText(getString(R.string.introduce_blur));

        } else if (this instanceof AutoLineActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("自动换行 Demo");
            tv_introduce.setText(getString(R.string.introduce_auto_line));

        } else if (this instanceof DivideLoadActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("分批加载 Demo");
            tv_introduce.setText(getString(R.string.introduce_divide_load));

        } else if (this instanceof AnimClickActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("点击动画 Demo");
            tv_introduce.setText(getString(R.string.introduce_anim_click));

        } else if (this instanceof FreedomListActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("非约束列表 Demo");
            tv_introduce.setText(getString(R.string.introduce_freedom));

        } else if (this instanceof NounProgressActivity) {
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("节点进度条 Demo");
            tv_introduce.setText(getString(R.string.introduce_noun_progress));

        }
    }

    /**
     * 处理View点击事件
     */
    private void initViewClick() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_back:
                        finish();
                        break;
                    case R.id.iv_introduce:
                        showIntroduce(rl_introduce);
                        break;
                    case R.id.iv_introduce_close:
                        hideIntroduce(rl_introduce);
                        break;
                }
            }
        };

        if (iv_back != null)
            iv_back.setOnClickListener(clickListener);

        if (iv_introduce != null)
            iv_introduce.setOnClickListener(clickListener);

        if (iv_introduce_close != null)
            iv_introduce_close.setOnClickListener(clickListener);
    }

    /**
     * 按键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_introduce != null && rl_introduce.getVisibility() == View.VISIBLE) {
                hideIntroduce(rl_introduce);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isCreate)
            create();

    }

    protected void create() {
        isCreate = false;
        findView();
        setListener();
        init();
    }

    protected abstract void findView();

    protected abstract void setListener();

    protected abstract void init();

    //==============================================================================================
    //======================= 以 下 是 关 于 手 势 右 滑 关 闭 ========================================
    //==============================================================================================

    /**
     * 绑定手势
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mUtilGesture && mUtilGesture.motionEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开启滑动关闭界面
     *
     * @param open
     */
    protected void openSlideFinish(boolean open) {
        if (mUtilGesture == null) {
            return;
        }
        mUtilGesture.openSlideFinish(open);
    }

    /**
     * 抬起关闭
     *
     * @param upFinish 【true：手指抬起后再关闭页面】
     *                 【false：进度条圆满就立刻关闭页面】
     */
    public void setUpFinish(boolean upFinish) {
        if (mUtilGesture == null) {
            return;
        }
        mUtilGesture.setUpFinish(upFinish);
    }

    /**
     * 设置进度条颜色
     */
    public void setProgressColor(int color) {
        if (mUtilGesture != null)
            mUtilGesture.setProgressColor(color);
    }

    /**
     * 滑动View
     * 【滑动过程中会移动的View】
     */
    public void setMoveView(View SlideView) {
        mUtilGesture.setRootView(SlideView);
    }


    //==============================================================================================
    //======================= 以 下 是 关 于 界 面 跳 转 动 画 ========================================
    //==============================================================================================

    /**
     * 打开新Activity
     *
     * @param intent  intent
     * @param animIn  新Activity进入的动画
     * @param animOut 当前Activity退出的动画
     */
    public void startActivity(Intent intent, int animIn, int animOut) {
        super.startActivity(intent);
        overridePendingTransition(animIn, animOut);
    }

    /**
     * 打开新的Activity
     *
     * @param intent intent
     * @param isAnim 是否开启过渡动画
     */
    public void startActivity(Intent intent, boolean isAnim) {
        if (isAnim) {
            startActivity(intent, R.anim.act_right_in, R.anim.act_left_out);
        } else {
            super.startActivity(intent);
        }
    }

    /**
     * 打开Activity
     *
     * @param intent intent
     */
    @Override
    public void startActivity(Intent intent) {
        startActivity(intent, true);
    }

    /**
     * 退出Activity
     */
    @Override
    public void finish() {
        finish(true);
    }

    /**
     * 退出Activity
     *
     * @param animIn  老Activity进入的动画
     * @param animOut 当前Activity退出的动画
     */
    public void finish(int animIn, int animOut) {
        if (rl_introduce != null && rl_introduce.getVisibility() == View.VISIBLE) {
            hideIntroduce(rl_introduce);
            return;
        }

        super.finish();
        if (false == this instanceof MainActivity)
            overridePendingTransition(animIn, animOut);
    }

    /**
     * 退出Activity
     *
     * @param isAnim 是否开启过渡动画
     */
    public void finish(boolean isAnim) {
        if (isAnim) {
            finish(R.anim.act_left_in, R.anim.act_right_out);
        } else {
            if (rl_introduce != null && rl_introduce.getVisibility() == View.VISIBLE) {
                hideIntroduce(rl_introduce);
                return;
            }

            super.finish();
        }
    }

    /**
     * 设置沉浸TitleBar
     *
     * @param topView
     */
    protected void setImmerseTitleBar(View topView) {
        mWantUtil.setImmerseTitleBar(topView);
    }

    /**
     * 展开介绍
     */
    protected void showIntroduce(View rl_introduce) {
        mWantUtil.showIntroduce(rl_introduce);
    }

    /**
     * 关闭介绍
     */
    protected void hideIntroduce(View rl_introduce) {
        mWantUtil.hideIntroduce(rl_introduce);
    }

}
