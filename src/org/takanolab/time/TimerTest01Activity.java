/**
 * 前回と今回の起動時間の差を求め，また起動時間を加算し，<br>
 * 各リストの生存時間と比較しリアルタイムに更新していく
 * 
 * @author s0921122
 */

package org.takanolab.time;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TimerTest01Activity extends Activity {
	
	ArrayList<MyField> nulllist;
	private static final String PREFEREMCE_NAME = "TimerTest";
	private static final String PREFERENCE_ACT = "ACTIVATION_TIME";
	private static final String PREFERENCE_TOTAL = "TOTAL_TIME";
	private static final String TAG = "TimerTest01";
	Handler handler = new Handler();
	long oldTime,nowTime;
	TextView outList,console;
	boolean deletePreference = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 起動時の時間
		long createTime = System.currentTimeMillis();
		Log.i(TAG,"create");
		// ID登録
		console = (TextView) findViewById(R.id.Console);
		console.setText("");
		outList = (TextView)findViewById(R.id.List);
		outList.setText("");
		
		// リスト
		nulllist = new ArrayList<MyField>();
		// リストに値をセット
		setArray(nulllist);
		
		// タイマー作成
		Timer countTimer = new Timer(true);
		// タイマーのスケジュール作成（1秒ごとにスケジュール実行）
		countTimer.schedule( new TimerTask(){
	        @Override
	        public void run() {
	            // Handlerを通じてUI Threadへ処理をキューイング
	            handler.post( new Runnable() {
	                public void run() {
	                	Log.i("Timer","running");
	                	// 今の時間と起動時間を計算
	                	nowTime += 1000;
	                	// 表示更新
	                	PrintUtil(nowTime);
	                	PrintList(nowTime);
	                }
	            });
	        }
	    }, 1000, 1000);

		// 前回終了時間を取得
		long[] lastTime = PreferenceConect();
		long totalTime = lastTime[0];
		oldTime = lastTime[1];
		
		//oldthme = System.currentTimeMillis();
		// 前回と今回の時間の差を計算
		long remainder = createTime - oldTime;

		// 累計時間を計算
		nowTime = totalTime + remainder;
		
		// 結果を表示
		PrintUtil(nowTime);
		PrintList(nowTime);
	}

	@Override
	protected void onDestroy() {
		// 終了時間を保存
		PreferenEdit();
		
		if(deletePreference)
			PreferenceClear();
		Log.i(TAG,"Destroy");
		
		super.onDestroy();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // メニューアイテムの追加
        menu.add(Menu.NONE, 1, Menu.NONE, "Reset").setIcon(android.R.drawable.ic_menu_add);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	// addしたときのIDで識別
        switch (item.getItemId()) {
        case 1:
        	deletePreference = true;
        	finish();
        }
        return false;
    }
    
	private void PreferenEdit(){
		SharedPreferences pref = getSharedPreferences(PREFEREMCE_NAME,MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		Editor e = pref.edit();
		e.putLong(PREFERENCE_TOTAL, nowTime);
		e.putLong(PREFERENCE_ACT, System.currentTimeMillis());
		e.commit();
	}
	
	private long[] PreferenceConect(){
		SharedPreferences pref = getSharedPreferences(PREFEREMCE_NAME,MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
		long[] ret = new long[2];
		ret[0] = pref.getLong(PREFERENCE_TOTAL, 0);
		ret[1] = pref.getLong(PREFERENCE_ACT, System.currentTimeMillis());
		return ret;
	}
	
	private void PreferenceClear(){
        SharedPreferences prefs = getSharedPreferences(PREFEREMCE_NAME, MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
	}

	private void setArray(ArrayList<MyField> list){
		list.add(new MyField("one",   10000));
		list.add(new MyField("two",   20000));
		list.add(new MyField("three", 30000));
		list.add(new MyField("four",  40000));
		list.add(new MyField("five",  50000));
		list.add(new MyField("six",   60000));
		list.add(new MyField("seven", 70000));
		list.add(new MyField("eight", 80000));
		list.add(new MyField("nine",  90000));
	}

	private void PrintUtil(long time){
		//console.setText("前回終了時間" + oldthme + "\n");
		//console.append("今回起動時間" + createTime + "\n");
		console.setText("時間は\n");
		//console.append(remainder / 1000 / 60 / 60 + "時間=\n");
		console.append(time / 1000 / 60 + "分 = ");
		console.append(time / 1000 + "秒 = ");
		console.append(time + "ミリ秒\n");
		console.append("経ちました.");
	}
	
	private void PrintList(long time){
		outList.setText("生存状況\n");
		
		for(int i = 0; nulllist.size() > i;i++){
			MyField temp = nulllist.get(i);
			if(temp.getLimitTime() <= time){
				// 生存時間を上回ったらDead
				temp.setStatus("Dead");
			}
			// リストの内容表示
			outList.append(temp.getName() + " : " + temp.getLimitTime() + " : " + temp.getStatus() + "\n");
		}
		
		outList.append("end.");
	}

	/**
	 * 値を保持するクラス
	 * 
	 * @author s0921122
	 *
	 */
	class MyField{
		private String name;
		private long limitTime;
		private String status;

		public MyField() {
			name = "";
			limitTime = 0;
			status = "";
		}
		public MyField(String name, long time){
			this.name = name;
			this.limitTime = time;
			status = "alive";
		}

		public void set(String name, long time){
			this.name = name;
			this.limitTime = time;
		}

		public void setStatus(String status){
			this.status = status;
		}
		public String getName() {
			return name;
		}

		public long getLimitTime() {
			return limitTime;
		}

		public String getStatus() {
			return status;
		}
	}
	
}