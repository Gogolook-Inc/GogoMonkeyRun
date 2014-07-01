
package com.android.monkeyrunner.recorder.actions;

import org.python.google.common.collect.BiMap;
import org.python.google.common.collect.ImmutableBiMap;

import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.android.monkeyrunner.MonkeyDevice;

public class AdvancePressAction implements Action {
	public static String[] KEYS = {
		"BACK", "MENU", "HOME", "SEARCH",
	};
	public static final BiMap<String, String> DOWNUP_FLAG_MAP =
			ImmutableBiMap.of(MonkeyDevice.DOWN_AND_UP, "Press",
					MonkeyDevice.DOWN, "Down",
					MonkeyDevice.UP, "Up");
	private final String key;
	private final String downUpFlag;

	public AdvancePressAction(String key, String downUpFlag) {
		this.key = key;
		this.downUpFlag = downUpFlag;
	}

	public AdvancePressAction(String key) {
		this(key, MonkeyDevice.DOWN_AND_UP);
	}

	@Override
	public String getDisplayName() {
		return String.format("%s button %s",
				DOWNUP_FLAG_MAP.get(downUpFlag), key);
	}

	@Override
	public String serialize() {
		String pydict = PyDictUtilBuilder.newBuilder().
				add("name", key).
				add("type", downUpFlag).build();
		return "PRESS|" + pydict;
	}

	@Override
	public void execute(IChimpDevice device) {
		device.press(key, TouchPressType.fromIdentifier(downUpFlag));
	}
}
