package com.slidingmenu.gridview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.GJTools.GanjiLifeCaserunnerActivity;
import com.GJTools.GanjiLife_RunnerResultActivity;
import com.jira.Httpdemo;
import com.zzw.test.R;
import com.zzw.test.info;
import com.zzw.test.caseinfo.Caseinfo;

public class GJTools extends Fragment {
	ArrayList<Gridinfo> mGridinfos = new ArrayList<Gridinfo>();
	Context mContext;
	Activity mActivity;
	GridView gview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setGridview();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.girdview, null);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void setGridview() {
		gview = (GridView) mActivity.findViewById(R.id.gview);
		//获取数据
		setInfo();
		//新建适配器
		gview.setAdapter(new gridadapter());
		//处理点击事件
		gview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long ID) {
				// TODO Auto-generated method stub
				Gridinfo gridinfo = mGridinfos.get(position);
				Intent mIntent = new Intent();
				switch (gridinfo.getTitle()) {
				case "执行case":
					mIntent = new Intent();
					mIntent.setClass(mContext, GanjiLifeCaserunnerActivity.class);
					startActivity(mIntent);
					break;
				case "测试结果分析":
					mIntent = new Intent();
					mIntent.setClass(mContext, GanjiLife_RunnerResultActivity.class);
					startActivity(mIntent);
					break;
				case "百宝箱0":
					Httpdemo.getv(mContext);
					break;
				default:
					break;
				}
			}
		});
	}

	public void setInfo() {
		mGridinfos.clear();
		int i = 0;
		while (i < 19) {
			Gridinfo information = new Gridinfo();
			information.setid(1000 + i);
			information.setTitle("百宝箱" + i);
			information.setImageID(R.drawable.ganjilife_icon_1);
			mGridinfos.add(information); // 将新的info对象加入到信息列表中
			i++;
		}

		//CaseRunner
		Gridinfo information = new Gridinfo();
		information.setid(Integer.parseInt(ToolsInfo.CASERUNNER_INFO[0]));
		information.setTitle(ToolsInfo.CASERUNNER_INFO[1]);
		information.setImageID(Integer.parseInt(ToolsInfo.CASERUNNER_INFO[2]));
		mGridinfos.add(information); // 将新的info对象加入到信息列表中

		//CaseRunner
		information = new Gridinfo();
		information.setid(Integer.parseInt(ToolsInfo.CASERUNNER_INFO[0]));
		information.setTitle(ToolsInfo.CASERUNNER_INFO[1]);
		information.setImageID(Integer.parseInt(ToolsInfo.CASERUNNER_INFO[2]));
		mGridinfos.add(information); // 将新的info对象加入到信息列表中

		//执行结果分析
		information = new Gridinfo();
		information.setid(Integer.parseInt(ToolsInfo.CASE_RESULT_ANALYZE[0]));
		information.setTitle(ToolsInfo.CASE_RESULT_ANALYZE[1]);
		information.setImageID(Integer.parseInt(ToolsInfo.CASE_RESULT_ANALYZE[2]));
		mGridinfos.add(information); // 将新的info对象加入到信息列表中
	}

	public class gridadapter extends BaseAdapter {
		private LayoutInflater inflater;

		@Override
		public int getCount() {
			return mGridinfos.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.gird_item, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.GVItemImage);
				holder.tv = (TextView) convertView.findViewById(R.id.GVItemText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv.setImageResource(mGridinfos.get(position).getImageID());
			holder.tv.setText(mGridinfos.get(position).getTitle());
			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView tv;
		}

	}
}
