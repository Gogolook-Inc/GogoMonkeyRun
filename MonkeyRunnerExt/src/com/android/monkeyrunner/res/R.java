
package com.android.monkeyrunner.res;

import java.util.Locale;

public class R {
	public static class string {
		public static final String menu_file = Locale.getDefault().equals(Locale.TAIWAN) ? "檔案" : "File";
		public static final String menu_function = Locale.getDefault().equals(Locale.TAIWAN) ? "功能" : "Function";
		public static final String menu_help = Locale.getDefault().equals(Locale.TAIWAN) ? "幫助" : "Help";

		public static final String menu_file_export = Locale.getDefault().equals(Locale.TAIWAN) ? "匯出記錄" : "Export Actions";

		public static final String menu_help_tutorial = Locale.getDefault().equals(Locale.TAIWAN) ? "教學" : "Tutorial";
		public static final String menu_help_about = Locale.getDefault().equals(Locale.TAIWAN) ? "關於" : "About";

		public static final String button_file_export = menu_file_export;
		public static final String button_wait = Locale.getDefault().equals(Locale.TAIWAN) ? "等待" : "Wait";
		public static final String button_press_a_button = Locale.getDefault().equals(Locale.TAIWAN) ? "點擊按鈕" : "Press a Button";
		public static final String button_type_something = Locale.getDefault().equals(Locale.TAIWAN) ? "鍵入字母" : "Type Something";
		public static final String button_filng = Locale.getDefault().equals(Locale.TAIWAN) ? "滑移" : "Fling";
		public static final String button_take_snapshot = Locale.getDefault().equals(Locale.TAIWAN) ? "螢幕截圖" : "Take Snapshot";
		public static final String button_reset_last_action = Locale.getDefault().equals(Locale.TAIWAN) ? "取消上一動作" : "Reset Last Action";
		public static final String button_rotate_display = Locale.getDefault().equals(Locale.TAIWAN) ? "轉置畫面" : "Rotate Display";
		public static final String button_refresh_display = Locale.getDefault().equals(Locale.TAIWAN) ? "更新畫面" : "Refresh Display";

		public static final String text_how_many_second_to_wait = Locale.getDefault().equals(Locale.TAIWAN) ? "等待幾秒?" : "How many seconds to wait?";
		public static final String text_what_button = Locale.getDefault().equals(Locale.TAIWAN) ? "點擊什麼按鈕?" : "What button to press?";
		public static final String text_what_to_type = Locale.getDefault().equals(Locale.TAIWAN) ? "鍵入什麼?" : "What to type?";
		public static final String text_which_direction = Locale.getDefault().equals(Locale.TAIWAN) ? "往哪個方向滑移?" : "Which Direction to fling?";
		public static final String text_how_long_to_drag = Locale.getDefault().equals(Locale.TAIWAN) ? "滑移多久(in ms)?" : "How long to drag (in ms)?";
		public static final String text_how_many_steps = Locale.getDefault().equals(Locale.TAIWAN) ? "滑移次數?" : "How many steps to do it in?";

		public static final String dialog_export_complete = Locale.getDefault().equals(Locale.TAIWAN) ? "匯出完成" : "Export Complete";

	}
}
