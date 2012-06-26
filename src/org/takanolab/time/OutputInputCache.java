package org.takanolab.time;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

public class OutputInputCache {

	private static final String TAG = "OutputInputCahe";
	private HashMap<String, CacheCore> cacheMap = new HashMap<String, CacheCore >();
	//private HashMap<String, String> tempMap = new HashMap<String, String>();
	private String path = "/sdcard/modeldata/3dcgmodel.obj";

	@SuppressWarnings("unchecked")
	public OutputInputCache(){
		cacheMap = (HashMap<String, CacheCore>) read_object(path);
	}

	public void OutPutCache(){
		write_object(cacheMap, path);
	}
	
	public void setCache(){
		cacheMap.put("one",   new CacheCore(new ByteArrayInputStream("alive".getBytes()),10000));
		cacheMap.put("two",   new CacheCore(new ByteArrayInputStream("alive".getBytes()),20000));
		cacheMap.put("three", new CacheCore(new ByteArrayInputStream("alive".getBytes()),30000));
		cacheMap.put("four",  new CacheCore(new ByteArrayInputStream("alive".getBytes()),40000));
		cacheMap.put("five",  new CacheCore(new ByteArrayInputStream("alive".getBytes()),50000));
		cacheMap.put("six",   new CacheCore(new ByteArrayInputStream("alive".getBytes()),60000));
		cacheMap.put("seven", new CacheCore(new ByteArrayInputStream("alive".getBytes()),70000));
		cacheMap.put("eight", new CacheCore(new ByteArrayInputStream("alive".getBytes()),80000));
		cacheMap.put("nine",  new CacheCore(new ByteArrayInputStream("alive".getBytes()),90000));
	}

	public boolean isCacheMap(){
		if(cacheMap != null){
			return true;
		}else{
			return false;
		}
	}
	public boolean isModelCache(String name){
		if(cacheMap.containsKey(name)){
			return true;
		}else{
			return false;
		}
	}
	public int getSize(){
		return cacheMap.size();
	}

	public void setModelCache(String name,CacheCore cc){
		cacheMap.put(name, cc);
	}

	public CacheCore getModelCacheCore(String name){
		if(cacheMap.containsKey(name)){
			return cacheMap.get(name);
		}else{
			return null;
		}
	}
	public boolean DeleteModelCache(String name){
		try{
			cacheMap.remove(name);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	public void clearCacheMap(){
		cacheMap.clear();
	}

	public HashMap<String, CacheCore> getCaheMap(){
		return cacheMap;
	}
	public Iterator<String> getMapIterator(){
		return cacheMap.keySet().iterator();
	}

	public static boolean write_object(Object obj,String file){
		try {
			FileOutputStream outFile = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(outFile);
			out.writeObject(obj);
			out.close();
			outFile.close();
		} catch(Exception e) {
			Log.d(TAG,"write");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Object read_object(String file){
		Object obj=new Object();
		try {
			FileInputStream inFile = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(inFile);
			obj = in.readObject();
			in.close();
			inFile.close();
		} catch(Exception e) {
			Log.d(TAG,"read");
			e.printStackTrace();
		}
		return obj;
	}

}
class CacheCore implements Serializable{

	private static final long serialVersionUID = 1L;
	private transient InputStream modelCache;
	private String modelCachetoString;
	private long beginTime;

	public CacheCore(){

	}
	public CacheCore(InputStream is,long time){
		this.modelCache = is;
		this.beginTime = time;
		changeInputStream();
	}

	public InputStream getModelCache() {
		return modelCache;
	}
	public void setModelCache(InputStream modelCache) {
		this.modelCache = modelCache;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public boolean isModelCache(){
		if(this.modelCache != null){
			return true;
		}else{
			return false;
		}
	}

	public void changeInputStream(){
		modelCachetoString = modelCache.toString();
	}
	public void changeString(){
		modelCache = new ByteArrayInputStream(modelCachetoString.getBytes());
	}
}
