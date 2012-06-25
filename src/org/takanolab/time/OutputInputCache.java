package org.takanolab.time;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

public class OutputInputCache {

	private static final String TAG = "OutputInputCahe";
	private HashMap<String, InputStream> cacheMap = new HashMap<String, InputStream>();
	private HashMap<String, String> tempMap = new HashMap<String, String>();
	private String path = "/sdcard/modeldata/3dcgmodelcache.txt";

	public OutputInputCache(){
			InputCacheFile();
	}

	@SuppressWarnings("unchecked")
	public void InputCacheFile(){
		Log.d(TAG,"Input Start");
		try{
			File inputFile = new File(path);
			if(inputFile.exists()){
				// InputStream作成
				FileInputStream fis = new FileInputStream(inputFile);
				ObjectInputStream ois = new ObjectInputStream(fis);

				// oisからオブジェクトを入力。
				tempMap = (HashMap<String, String>) ois.readObject();
				Iterator<String> itr = tempMap.keySet().iterator();

				// 中身を取り出しcacheに格納
				while(itr.hasNext()){
					String key = itr.next();
					InputStream tempIs = new ByteArrayInputStream(tempMap.get(key).getBytes());
					cacheMap.put(key, tempIs);
				}

				// 入力ストリームを閉じる。
				fis.close();
				ois.close();
			}else{
				inputFile.createNewFile();
			}
		}catch (Exception e) {
			Log.e(TAG,e.toString());
		}finally{
			Log.d(TAG,"Input End");
		}
	}

	public void OutputCacheFile(){
		Log.d(TAG,"Output Start");
		try{
			// 出力ストリームの生成。
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// マップクリア
			tempMap.clear();
			Iterator<String> itr = cacheMap.keySet().iterator();
			
			// 中身を取り出しtempMapへ格納
			while(itr.hasNext()){
				String key = itr.next();
				tempMap.put(key,cacheMap.get(key).toString());
			}
			
			// マップを整列化し、oosに書き込む
			oos.writeObject(tempMap);
			// 出力ストリームを閉じる。
			fos.close();
			oos.close();
		}catch (Exception e) {
			Log.e(TAG,e.toString());
		}finally{
			Log.d(TAG,"Output End");
		}
	}

	public void OutputCacheFile(HashMap<String, ?> map){
		Log.d(TAG,"Output Start");
		try{
			// 出力ストリームの生成。
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// マップクリア
			tempMap.clear();
			Iterator<String> itr = map.keySet().iterator();
			
			// 中身を取り出しtempMapへ格納
			while(itr.hasNext()){
				String key = itr.next();
				tempMap.put(key,map.get(key).toString());
			}
			
			// マップを整列化し、oosに書き込む
			oos.writeObject(tempMap);
			// 出力ストリームを閉じる。
			fos.close();
			oos.close();
		}catch (Exception e) {
			Log.e(TAG,e.toString());
		}finally{
			Log.d(TAG,"Output End");
		}
	}
	
	public InputStream isModelCache(String name){
		if(cacheMap.containsKey(name)){
			return cacheMap.get(name);
		}else{
			return null;
		}
	}
	
	public boolean isCacheMap(){
		if(cacheMap != null){
			return true;
		}else{
			return false;
		}
	}
	
	public void setCacheMap(String name,InputStream is){
		cacheMap.put(name, is);
	}
	public void clearCacheMap(){
		cacheMap.clear();
	}

	public HashMap<String, InputStream> getCaheMap(){
		return cacheMap;
	}
}
