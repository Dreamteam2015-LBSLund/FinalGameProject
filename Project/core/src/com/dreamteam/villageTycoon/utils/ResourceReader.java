package com.dreamteam.villageTycoon.utils;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;

public class ResourceReader {
	
	// Wiki (in swedish): https://github.com/Dreamteam2015-LBSLund/Village-Tycoon-RTS/wiki/ResourceReader
	
	private HashMap<String, String> data;
	private String filename;
	private String name;
	
	//if there's only one object in the file
	public ResourceReader(FileHandle f) {
		this(removeWhitespace(f.readString()));
	}
	
	//for a string of one object
	private ResourceReader(String data) {
		if (data.endsWith("}")) data = data.substring(0, data.length() - 2);
		String[] s = data.split("\\:\\{");
		if (s.length != 2) System.out.println("Failed to split the string " + data);
		this.name = s[0];
		this.data = readData(s[1]);
	}
	
	// read a file of one or more objects
	public static ResourceReader[] readObjectList(FileHandle f) {
		if (f == null || !f.exists()) {
			System.out.println("WARNING: couldn't find file " + f.name());
			return new ResourceReader[0];
		} else {
			String data = f.readString();
			if (data.endsWith("}")) data = data.substring(0, data.length() - 2);
			
			String[]     objects = removeWhitespace(data).split("}");
			ResourceReader[] out = new ResourceReader[objects.length];
			
			for (int i = 0; i < objects.length; i++) {
				out[i] = new ResourceReader(objects[i]);
			}
			
			return out;
		}
	}
	
	private static String removeWhitespace(String s) {
		return s.replaceAll("\\s+","");
	}
	
	public String getObjectName() {
		return name;
	}
	
	public String getString(String name) {
		if (!data.containsKey(name)) {
			System.out.println("WARNING: Value " + name + " not found in " + filename + "\nExisting keys are:");
			for (String s : getAllKeys()) System.out.println(s);
			return null;
		}
		return data.get(name).replaceAll("\\s+$", "");
	}
	
	public float getFloat(String name) {
		return Float.parseFloat(getString(name).replaceAll("\\s+",""));
	}
	
	public int getInt(String name) {
		return Integer.parseInt(getString(name).replaceAll("\\s+",""));
	}
	
	public boolean getBool(String name) {
		return Boolean.parseBoolean(getString(name).replaceAll("\\s+",""));
	}
	
	public String[] getList(String name) {
		return getList(name, false);
	}
	
	public int[] getIntList(String name) {
		String[] s = getList(name);
		int[] ints = new int[s.length];
		try {
			for (int i = 0; i < s.length; i++) ints[i] = Integer.parseInt(s[i]); 
		} catch (Exception e) {
			System.out.println("ERROR: formatting error in list " + name + ", couldn't parse to ints");
		}
		return ints;
	}
	
	public String[] getList(String name, boolean removeWhitespace) {
		if (!data.containsKey(name)) {
			System.out.println("WARNING: couldn't find list " + name + " in object " + this.name);
			return new String[0];
		}
		String[] d = getString(name).split(",");
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
	
	// if the object is a string already
	private static HashMap<String, String> readData(String data) {
		HashMap<String, String> out = new HashMap<String, String>();
		
		if (data.endsWith(";")) data = data.substring(0, data.length() - 1);
		
		String[] properties = data.split(";");
		
		for (String s : properties) {
			String[] ss = s.split(":");
			//System.out.println(ss[0]);
			out.put(removeWhitespace(ss[0]), removeWhitespace(ss[1]));
		}
		
		return out;
	}
}