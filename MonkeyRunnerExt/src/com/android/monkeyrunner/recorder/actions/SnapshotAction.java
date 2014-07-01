
package com.android.monkeyrunner.recorder.actions;

import com.android.chimpchat.core.IChimpDevice;

public class SnapshotAction implements Action {

	@Override
	public void execute(IChimpDevice arg0) throws Exception {
	}

	@Override
	public String getDisplayName() {
		return String.format("Take a snapshot");
	}

	@Override
	public String serialize() {
		return "TAKE SNAPSHOT";
	}

}
