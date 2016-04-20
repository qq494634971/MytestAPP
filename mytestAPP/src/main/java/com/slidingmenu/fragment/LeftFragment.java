package com.slidingmenu.fragment;

import android.R.integer;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zzw.test.MainActivity;
import com.zzw.test.R;
/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View todayView;
	private View lastListView;
	private View discussView;
	private View favoritesView;
	private View commentsView;
	private View settingsView;
	int right_btn_visiable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		todayView = view.findViewById(R.id.tvToday);
		lastListView = view.findViewById(R.id.tvLastlist);
		discussView = view.findViewById(R.id.tvDiscussMeeting);
		favoritesView = view.findViewById(R.id.tvMyFavorites);
		commentsView = view.findViewById(R.id.tvMyComments);
		settingsView = view.findViewById(R.id.tvMySettings);
		
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		commentsView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		right_btn_visiable = View.GONE;
		String right_btn_text = "编辑";
		switch (v.getId()) {
		case R.id.tvToday: // 今日
			newContent = new IMTest();
			title = getString(R.string.today);
			right_btn_visiable = View.VISIBLE;
			right_btn_text = getResources().getString(R.string.executeSomeCase);
			break;
		case R.id.tvLastlist:// 往期列表
			newContent = new LastListFragment();
			title = getString(R.string.lastList);
			break;
		case R.id.tvDiscussMeeting: // 讨论集会
			newContent = new DiscussMeeting();
			title = getString(R.string.discussMeetting);
			break;
		case R.id.tvMyFavorites: // 我的收藏
			newContent = new MyFavoritesFragment();
			title = getString(R.string.myFavorities);
			break;
		case R.id.tvMyComments: // 我的评论
			newContent = new MyCommentsFragment();
			title = getString(R.string.myComments);
			break;
		case R.id.tvMySettings: // 设置
			newContent = new MySettingsFragment();
			title = getString(R.string.settings);
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title, right_btn_visiable, right_btn_text);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title, int right_btn_visiable, String right_btn_text) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title, right_btn_visiable, right_btn_text);
		}
	}
	
}
