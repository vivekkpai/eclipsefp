package net.sf.eclipsefp.haskell.core.partitioned.runner;

import java.io.InputStream;

public class AlexRunner extends PartitionedRunner {

	@Override
	public String getExecutableName() {
		return "alex";
	}

	@Override
	public InputStream selectStream(Process p) {
		return p.getErrorStream();
	}

}