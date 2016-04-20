package com.slidingmenu.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zzw.test.CaserunnerTask;
import com.zzw.test.R;
import com.zzw.test.info;
import com.zzw.test.caseinfo.Caseinfo;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author wuwenjie
 * @description 今日
 */
public class IMTest extends Fragment {
	Context mContext;
	Activity mActivity;
	ListView listView;
	TextView rightTextBtn;
	List<info> mlistInfo = new ArrayList<info>();
	StringBuffer mStringBuffer = new StringBuffer();
	String[] mIDs;
	ArrayList<Boolean> mcheckboxstate = new ArrayList<Boolean>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setList(View.GONE);
		rightTextBtn = (TextView) mActivity.findViewById(R.id.right_btn);
		rightTextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rightTextBtn.getText().toString().equals(getResources().getString(R.string.executeSomeCase))) {
					rightTextBtn.setText(getResources().getString(R.string.finish));
					setList(View.VISIBLE);
				} else if (rightTextBtn.getText().toString().equals(getResources().getString(R.string.finish))) {
					rightTextBtn.setText(getResources().getString(R.string.executeSomeCase));
					for (int i = 0; i < mcheckboxstate.size(); i++) {
						if (mcheckboxstate.get(i)) {
							mStringBuffer.append(Caseinfo.case1[i][0]+";");
						}
					}
					if (mStringBuffer.length()!=0) {
						mIDs = new String[getCount(mStringBuffer.toString(), ";")];
						mStringBuffer.delete(mStringBuffer.length()-1, mStringBuffer.length());
						mStringBuffer.append("  马上开始执行");
						for (int i = 0,j = 0; i < mcheckboxstate.size(); i++) {
							if (mcheckboxstate.get(i)) {
								mIDs[j] = Caseinfo.case1[i][1];
								j++;
							}
						}
						CaserunnerTask mCaserunner = new CaserunnerTask(mContext, mIDs);
						mCaserunner.execute();
						setList(View.GONE);
						Toast.makeText(mContext, mStringBuffer.toString(), Toast.LENGTH_LONG).show();
						mStringBuffer.delete(0, mStringBuffer.length());
					}else {
						setList(View.GONE);
					}
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_today, null);
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

	public void setList(int checkbox_visiable) {
		listView = (ListView) mActivity.findViewById(R.id.listView);
		//初始化复选框选择状态
		mcheckboxstate.clear();
		for (int i = 0; i < Caseinfo.case1.length; i++) {
			mcheckboxstate.add(i, false);
		}
		setInfo(checkbox_visiable); // 给信息赋值函数

		listView.setAdapter(new ListViewAdapter(mlistInfo));

		// 处理Item的点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (rightTextBtn.getText().toString().equals(getResources().getString(R.string.executeSomeCase))) {
					// 如果右上角文案是executeSomeCase,点击执行一条case
					info getObject = mlistInfo.get(position); // 通过position获取所点击的对象
					int infoId = getObject.getId(); // 获取信息id
					String infoTitle = getObject.getTitle(); // 获取信息标题
					// String infoDetails = getObject.getDetails(); // 获取信息详情
					Toast.makeText(mContext, infoTitle+"  马上开始执行", Toast.LENGTH_SHORT).show();
					CaserunnerTask mCaserunner = new CaserunnerTask(mContext, Caseinfo.case1[position][1]);
					mCaserunner.execute();
				} else if (rightTextBtn.getText().toString().equals(getResources().getString(R.string.finish))) {
					// 如果右上角文案是finish,点击执行选择的case
					int index = listView.getFirstVisiblePosition();  
					View v = listView.getChildAt(0);  
					int top = (v == null) ? 0 : v.getTop();
					mcheckboxstate.set(position, !mcheckboxstate.get(position));
					setInfo(view.VISIBLE); //重新给信息赋值函数
					listView.setAdapter(new ListViewAdapter(mlistInfo));
					listView.setSelectionFromTop(index, top);
				}

			}
		});

		// //长按菜单显示
		// listView.setOnCreateContextMenuListener(new
		// OnCreateContextMenuListener() {
		// public void onCreateContextMenu(ContextMenu conMenu, View view ,
		// ContextMenuInfo info) {
		// conMenu.setHeaderTitle("菜单");
		// conMenu.add(0, 0, 0, "条目一");
		// conMenu.add(0, 1, 1, "条目二");
		// conMenu.add(0, 2, 2, "条目三");
		// }
		// });

	}

	// 长按菜单处理函数
	public boolean onContextItemSelected(MenuItem aItem) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();
		switch (aItem.getItemId()) {
		case 0:
			Toast.makeText(mContext, "你点击了条目一", Toast.LENGTH_SHORT).show();
			return true;
		case 1:
			Toast.makeText(mContext, "你点击了条目二", Toast.LENGTH_SHORT).show();

			return true;
		case 2:
			Toast.makeText(mContext, "你点击了条目三", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	public class ListViewAdapter extends BaseAdapter {
		View[] itemViews;

		public ListViewAdapter(List<info> mlistInfo) {
			// TODO Auto-generated constructor stub
			itemViews = new View[mlistInfo.size()];
			for (int i = 0; i < mlistInfo.size(); i++) {
				info getInfo = (info) mlistInfo.get(i); // 获取第i个对象
				// 调用makeItemView，实例化一个Item
				itemViews[i] = makeItemView(getInfo.getTitle(), getInfo.getDetails(), getInfo.getcheckbox_visiable(), getInfo.getcheckboxstate());
			}
		}

		public int getCount() {
			return itemViews.length;
		}

		public View getItem(int position) {
			return itemViews[position];
		}

		public long getItemId(int position) {
			return position;
		}

		// 绘制Item的函数
		private View makeItemView(String strTitle, String strText, int checkbox_visiable, boolean checkboxstate) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 使用View的对象itemView与R.layout.item关联
			View itemView = inflater.inflate(R.layout.listitem_imtest, null);

			// 通过findViewById()方法实例R.layout.item内各组件
			TextView title = (TextView) itemView.findViewById(R.id.title);
			title.setText(strTitle); // 填入相应的值
			TextView text = (TextView) itemView.findViewById(R.id.info);
			text.setText(strText);
			CheckBox checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
			checkbox.setVisibility(checkbox_visiable);
			checkbox.setChecked(checkboxstate);
			return itemView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// if (convertView == null)
			return itemViews[position];
			// return convertView;
		}
	}

	public void setInfo(int checkbox_visiable) {
		mlistInfo.clear();
		int i = 0;
		while (i < Caseinfo.case1.length) {
			info information = new info();
			information.setId(1000 + i);
			information.setTitle(Caseinfo.case1[i][0]);
			information.setDetails("详细信息" + i);
			information.setcheckbox_visiable(checkbox_visiable);
			information.setcheckboxstate(mcheckboxstate.get(i));
			mlistInfo.add(information); // 将新的info对象加入到信息列表中
			i++;
		}
	}

	/**
	 * 获取abc字符串在整个字符串中出现的次数。
	 */
	public static int getCount(String str,String sub)
	{
		int index = 0;
		int count = 0;
		while((index = str.indexOf(sub,index))!=-1)
		{
	
			index = index + sub.length();
			count++;
		}
		return count;
	}
	
}
