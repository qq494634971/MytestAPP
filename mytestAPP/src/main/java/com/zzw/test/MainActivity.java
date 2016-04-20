 package com.zzw.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.slidingmenu.fragment.IMTest;
import com.slidingmenu.fragment.LeftFragment;
import com.slidingmenu.gridview.GJTools;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements OnClickListener {

	private ImageView topButton;
	private Fragment mContent;
	private TextView topTextView;
	public TextView rightTextBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		topButton = (ImageView) findViewById(R.id.topButton);
		topButton.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		rightTextBtn = (TextView) findViewById(R.id.right_btn);
		initSlidingMenu(savedInstanceState);
//		InputStream is = null;
//		try {
//			is = getAssets().open("test_NewCase_0_pic_0002.jpg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	
	/**
	 * 初始化侧边栏
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		if (mContent == null) {
			mContent = new GJTools();
		}

		// 设置左侧滑动菜单
		setBehindContentView(R.layout.menu_frame_left);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commit();

		// 实例化滑动菜单对象
		SlidingMenu sm = getSlidingMenu();
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);

		switchConent(mContent, "赶集测试百宝箱", View.GONE, "");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	/**
	 * 切换Fragment
	 * 
	 * @param fragment
	 */
	public void switchConent(Fragment fragment, String title, int right_btn_visiable, String right_btn_text) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
		topTextView.setText(title);
		rightTextBtn.setVisibility(right_btn_visiable);
		rightTextBtn.setText(right_btn_text);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topButton:
			toggle();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			mContent = new GJTools();
			switchConent(mContent, "赶集测试百宝箱", View.GONE, "");
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

//	public void onCreatGridview() {
//		GridView gridview = (GridView) findViewById(R.id.gridview);
//
//		//生成动态数组，并且转入数据  
//		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < 10; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("ItemImage", R.drawable.ganji);//添加图像资源的ID  
//			map.put("ItemText", "NO." + String.valueOf(i));//按序号做ItemText  
//			lstImageItem.add(map);
//		}
//		//生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
//		SimpleAdapter saImageItems = new SimpleAdapter(this, //没什么解释  
//				lstImageItem,//数据来源   
//				R.layout.night_item,//night_item的XML实现  
//
//				//动态数组与ImageItem对应的子项          
//				new String[] { "ItemImage", "ItemText" },
//
//				//ImageItem的XML文件里面的一个ImageView,两个TextView ID  
//				new int[] { R.id.ItemImage, R.id.ItemText });
//		//添加并且显示  
//		gridview.setAdapter(saImageItems);
//		//添加消息处理  
//		gridview.setOnItemClickListener(new ItemClickListener());
//	}
//
//	//当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件  
//	class ItemClickListener implements OnItemClickListener {
//		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
//				View arg1,//The view within the AdapterView that was clicked  
//				int arg2,//The position of the view in the adapter  
//				long arg3//The row id of the item that was clicked  
//		) {
//			//在本例中arg2=arg3  
//			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//			//显示所选Item的ItemText  
//			setTitle((String) item.get("ItemText"));
//		}
//	}
}
