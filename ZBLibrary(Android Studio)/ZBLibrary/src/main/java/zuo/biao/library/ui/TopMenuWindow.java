/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.ui;

import java.util.ArrayList;
import java.util.Arrays;

import zuo.biao.library.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**通用顶部弹出菜单
 * @author lemon
 * @use toActivity或startActivityForResult > onActivityResult方法内data.getIntExtra(
 * SelectPictureActivity.RESULT_CLICKED_ITEM)可得到点击的position(int)
 * @must（intent.putExtra传值） TopMenuWindow.INTENT_NAMES
 */
public class TopMenuWindow extends Activity implements OnItemClickListener, OnClickListener{
	private static final String TAG = "TopMenuWindow";
	
	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动TopMenuWindow的Intent
	 * @param context
	 * @param title - 非必需
	 * @param names
	 * @return
	 */
	public static Intent createIntent(Context context, String[] names) {
		return createIntent(context, names, new ArrayList<Integer>());
	}

	/**启动TopMenuWindow的Intent
	 * @param context
	 * @param title - 非必需
	 * @param nameList
	 * @return
	 */
	public static Intent createIntent(Context context, ArrayList<String> nameList) {
		return createIntent(context, nameList, null);
	}

	/**启动TopMenuWindow的Intent
	 * @param context
	 * @param title - 非必需
	 * @param names
	 * @param intentCodes
	 * @return
	 */
	public static Intent createIntent(Context context, String[] names, int[] intentCodes) {
		return new Intent(context, TopMenuWindow.class).
				putExtra(INTENT_NAMES, names).
				putExtra(INTENT_INTENTCODES, intentCodes);
	}

	/**启动TopMenuWindow的Intent
	 * @param context
	 * @param title - 非必需
	 * @param names
	 * @param intentCodeList
	 * @return
	 */
	public static Intent createIntent(Context context, String[] names, ArrayList<Integer> intentCodeList) {
		return new Intent(context, TopMenuWindow.class).
				putExtra(INTENT_NAMES, names).
				putExtra(INTENT_INTENTCODES, intentCodeList);
	}

	/**启动TopMenuWindow的Intent
	 * @param context
	 * @param title - 非必需
	 * @param nameList
	 * @param intentCodeList
	 * @return
	 */
	public static Intent createIntent(Context context,
			ArrayList<String> nameList, ArrayList<Integer> intentCodeList) {
		return new Intent(context, TopMenuWindow.class).
				putStringArrayListExtra(INTENT_NAMES, nameList).
				putIntegerArrayListExtra(INTENT_INTENTCODES, intentCodeList);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	
	private boolean isActivityAlive;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_menu_window);

		isActivityAlive = true;

		init();
	}

	
	public static final String INTENT_NAMES = "INTENT_NAMES";
	public static final String INTENT_INTENTCODES = "INTENT_INTENTCODES";

	public static final String RESULT_NAME = "RESULT_NAME";
	public static final String RESULT_POSITION = "RESULT_POSITION";
	public static final String RESULT_INTENT_CODE = "RESULT_INTENT_CODE";
	
	private ArrayList<String> nameList = null;
	private ArrayList<Integer> intentCodeList = null;
	private ArrayAdapter<String> adapter;
	private ListView lvTopMenu;
	private View llTopMenuWindowBg;
	private void init() {

		llTopMenuWindowBg = findViewById(R.id.llTopMenuWindowBg);
		llTopMenuWindowBg.setOnClickListener(this);

		Intent intent = getIntent();
		
		int[] intentCodes = intent.getIntArrayExtra(INTENT_INTENTCODES);
		if (intentCodes == null || intentCodes.length <= 0) {
			intentCodeList = intent.getIntegerArrayListExtra(INTENT_INTENTCODES);
		} else {
			intentCodeList = new ArrayList<Integer>();
			for (int code : intentCodes) {
				intentCodeList.add(code);
			}
		}
		
		String[] menuItems = intent.getStringArrayExtra(INTENT_NAMES);
		if (menuItems == null || menuItems.length <= 0) {
			nameList = intent.getStringArrayListExtra(INTENT_NAMES);
		} else {
			nameList = new ArrayList<String>(Arrays.asList(menuItems));
		}

		if (nameList == null || nameList.size() <= 0) {
			Log.e(TAG, "init   nameList == null || nameList.size() <= 0 >> finish();return;");
			finish();
			return;
		}
		
		adapter = new ArrayAdapter<String>(this, R.layout.top_menu_list_item, R.id.tvTopMenuListItem, nameList);

		lvTopMenu = (ListView) findViewById(R.id.lvTopMenuWindowMenu);
		lvTopMenu.setAdapter(adapter);
		lvTopMenu.setOnItemClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.llTopMenuWindowBg) {
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Intent intent = new Intent().putExtra(RESULT_POSITION, position);
		if (intentCodeList != null && intentCodeList.size() > position) {
			intent.putExtra(RESULT_INTENT_CODE, intentCodeList.get(position));
		}

		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isActivityAlive) {
				finish();
				return true;
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		isActivityAlive = false;

		llTopMenuWindowBg.setEnabled(false);

		super.finish();
		overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
	}

}
