
package com.james.uicomparerunner.res;

import java.util.Locale;

public class R {
	public static class string {
		public static final String app_name = "Gogo Monkey Run";

		public static final String menu_file = Locale.getDefault().equals(Locale.TAIWAN) ? "檔案" : "File";
		public static final String menu_device = Locale.getDefault().equals(Locale.TAIWAN) ? "裝置" : "Device";
		public static final String menu_edit = Locale.getDefault().equals(Locale.TAIWAN) ? "修改" : "Edit";
		public static final String menu_help = Locale.getDefault().equals(Locale.TAIWAN) ? "幫助" : "Help";

		public static final String menu_file_generate_script = Locale.getDefault().equals(Locale.TAIWAN) ? "產生新紀錄檔與腳本" : "Generate a New Action Record";
		public static final String menu_file_edit_recorder = Locale.getDefault().equals(Locale.TAIWAN) ? "編輯紀錄檔" : "Edit Action Record";
		public static final String menu_file_choose_script = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇腳本" : "Choose Scripts";
		public static final String menu_file_run_last_script = Locale.getDefault().equals(Locale.TAIWAN) ? "執行上次腳本" : "Execute Last Scripts";
		public static final String menu_file_show_last_result = Locale.getDefault().equals(Locale.TAIWAN) ? "重看上次比較結果" : "Review Last Result";
		public static final String menu_file_clear = Locale.getDefault().equals(Locale.TAIWAN) ? "清除" : "Clear";
		public static final String menu_file_close = Locale.getDefault().equals(Locale.TAIWAN) ? "關閉" : "Close";
		public static final String menu_file_save = Locale.getDefault().equals(Locale.TAIWAN) ? "儲存" : "Save";

		public static final String menu_device_reset_device = Locale.getDefault().equals(Locale.TAIWAN) ? "變更預設裝置" : "Select Default Device";
		public static final String menu_device_reset_package_name = Locale.getDefault().equals(Locale.TAIWAN) ? "變更測試Package Name" : "Change Package Name";

		public static final String menu_open_editor = Locale.getDefault().equals(Locale.TAIWAN) ? "開啟編輯器" : "Open Editor";

		public static final String menu_help_tutorial = Locale.getDefault().equals(Locale.TAIWAN) ? "教學" : "Tutorial";
		public static final String menu_help_about = Locale.getDefault().equals(Locale.TAIWAN) ? "關於" : "About";

		public static final String dialog_title_alert = Locale.getDefault().equals(Locale.TAIWAN) ? "注意:" : "Alert:";
		public static final String dialog_title_quikly_choose = Locale.getDefault().equals(Locale.TAIWAN) ? "快速選擇" : "Quick Action";
		public static final String dialog_title_script_select = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇腳本" : "Choose Scripts";
		public static final String dialog_title_choose_sdk = Locale.getDefault().equals(Locale.TAIWAN) ? "請選擇 android-sdk 路徑" : "Set path of android-sdk";
		public static final String dialog_alert_lack_of_sdk = Locale.getDefault().equals(Locale.TAIWAN) ? "缺少或錯誤 android-sdk 路徑，\n請重新設定路徑" : "Error path for android-sdk.\nPlease reset path.";
		public static final String dialog_title_choose_a_device = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇裝置" : "Select a Device.";
		public static final String dialog_alert_choose_a_device = Locale.getDefault().equals(Locale.TAIWAN) ? "請連結手機或是模擬器，並選擇其中一支為預設裝置"
				: "Please confirm that any emulator/device is connected.\n Select a Device.";
		public static final String dialog_alert_no_devices = Locale.getDefault().equals(Locale.TAIWAN) ? "請確認是否已連接任何手機裝置或模擬器" : "Please confirm that any emulator/device is connected.";
		public static final String dialog_alert_find_action_file = "請選擇從\"monkey recorder模擬器\"中匯出的紀錄檔來產生腳本(副檔名為.mr)";
		public static final String dialog_alert_create_script_ok = "產生腳本成功，是否繼續編輯記錄檔";
		public static final String dialog_alert_file_disappear = "選擇檔案不存在，請重新確認";
		public static final String dialog_alert_create_a_new_script = "目前沒有測試腳本，是否產生一份新腳本?";
		public static final String dialog_alert_open_monkey_recorder = "按下確定後會開啟 monkey recorder，\n記錄完測試流程後記得按下\"Export Actions\"儲存紀錄，\n副檔名須為.mr";
		public static final String dialog_alert_set_as_target = "是否設定此圖片為正確結果?";
		public static final String dialog_alert_set_as_target_success = "已成功替換目標";
		public static final String dialog_alert_save_success = "儲存成功";
		public static final String dialog_alert_input_number = "請輸入數字";
		public static final String dialog_alert_input_package_name = "請輸入Package Name";
		public static final String dialog_alert_package_name_error = "Package Name不存在，請重新確認";
		public static final String dialog_alert_save_before_leaving = "是否儲存？";
		public static final String dialog_alert_run_last_script = "執行上次腳本";
		public static final String dialog_alert_run_script = "執行腳本";

		public static final String text_target = "正確截圖";
		public static final String text_test = "對照截圖";
		public static final String text_comparison = "比較結果";

		public static final String button_start_from_desktop = "從桌面開始測試";
		public static final String button_end_to_desktop = "結束返回桌面";
		public static final String button_add_while = "設定迴圈";
		public static final String button_save = "儲存腳本(script)";
		public static final String button_edit_preference = "編輯資料";
		public static final String button_clear_data = "清除資料";
		public static final String button_add_script = "增加腳本";
		public static final String button_remove_last_script = "取消上一步";
		public static final String button_clear_script = "清除腳本";
		public static final String button_run_script = "執行腳本";
	}
}
