package com.timvisee.minecraftrunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParser {

	List<String> args = new ArrayList<String>();
	
	public ArgsParser(String[] args) {
		this.args = Arrays.asList(args);
	}
	
	public ArgsParser(List<String> args) {
		this.args = args;
	}
	
	public List<String> getArgs() {
		return this.args;
	}
	
	public int getArgsCount() {
		return this.args.size();
	}
	
	public boolean isFlagSet(String flag) {
		for(int i = 0; i < this.args.size(); i++)
			if(this.args.get(i).trim().equals("-" + flag.trim()))
				if(i - 1 < this.args.size())
					return true;
		return false;
	}
	
	public String getFlag(String flag) {
		for(int i = 0; i < this.args.size(); i++)
			if(this.args.get(i).trim().equals("-" + flag.trim()))
				if(i - 1 < this.args.size())
					return this.args.get(i + 1);
		return "";
	}
}
