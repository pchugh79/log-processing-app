package com.mycompany.log.processor;

public enum ExecutionMode {

	EXECUTE("execute"),
	GENERATE_RECORDS("generate-records");

	private String mode;

	ExecutionMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public static ExecutionMode getInstance(String mode) {
		if (ExecutionMode.GENERATE_RECORDS.getMode().equals(mode)) {
			return ExecutionMode.GENERATE_RECORDS;
		}
		//default
		return ExecutionMode.EXECUTE;
	}

}
