package com.cmwebgame.entities.vo;

import com.cmwebgame.entities.Module;

public class ModuleV extends Module {
	
	private String fatherModuleName;

	public String getFatherModuleName() {
		return fatherModuleName;
	}

	public void setFatherModuleName(String fatherModuleName) {
		this.fatherModuleName = fatherModuleName;
	}

}
