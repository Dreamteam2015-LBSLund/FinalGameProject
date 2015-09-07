package com.dreamteam.villageTycoon.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ResourceReader {
	
	private HashMap<String, String> data;
	
	public ResourceReader(FileHandle f) {
		data = readResource(f);
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
		return getList(name, false);
	}
	
	public String[] getList(String name, boolean removeWhitespace) {
		String[] d = data.get(name).split(", ");
		if (removeWhitespace) {
			for (int i = 0; i < d.length; i++) {
				d[i] = d[i].replaceAll("\\s+","");
			}
		}
		return d;
	}
	
	public String[] getAllKeys() {
		return data.keySet().toArray(new String[data.size()]);
	}
	
	private HashMap<String, String> readResource(FileHandle f) {
		if (!f.exists()) System.out.println("This file does not exist!");
		//filename = Gdx.files.internal("assets/" + filename).file().getAbsolutePath().replace("desktop",  "core"); //TODO: test this thoroughly
		HashMap<String, String> out = new HashMap<String, String>();
		String read = f.readString();
		if (read == null) {
			System.out.println("there was an error loading tileType " + f + ", absolute path: " + f.file().getAbsolutePath());
			return null;
		}
		String[] in = read.split("\n");
		for (String s : in) {
			String[] ss = s.split(": ");
			out.put(ss[0], ss[1]);
		}
		return out;
	}
}