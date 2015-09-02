package com.dreamteam.villageTycoon.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

public class ResourceReader {
	
	private HashMap<String, String> data;
	
	public ResourceReader(String path) {
		data = readResource(path);
	}
	
	public String getString(String name) {
		return data.get(name);
	}
	
	public float getFloat(String name) {
		return Float.parseFloat(data.get(name).replaceAll("\\s+",""));
	}
	
	public int getInt(String name) {
		return Integer.parseInt(data.get(name).replaceAll("\\s+",""));
	}
	
	public boolean getBool(String name) {
		return Boolean.parseBoolean(data.get(name).replaceAll("\\s+",""));
	}
	
	public String[] getList(String name) {
		return data.get(name).split(", ");
	}
	
	//reads from the Assets folder. TODO: move to constructor and make read<type>(String property)
	private HashMap<String, String> readResource(String filename) {
		filename = Gdx.files.internal("assets/" + filename).file().getAbsolutePath().replace("desktop",  "core");
		HashMap<String, String> out = new HashMap<String, String>();
		String read = readFile(filename);
		if (read == null) {
			System.out.println("there was an error loading tileType " + filename);
			return null;
		}
		String[] in = read.split("\n");
		for (String s : in) {
			String[] ss = s.split(": ");
			out.put(ss[0], ss[1]);
		}
		return out;
	}
	
	private String readFile(String filename)
	{
	    String content = null;
	    File file = new File(filename); //for ex foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
	    }
	    return content;
	}
}
