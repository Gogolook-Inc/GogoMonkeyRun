
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
		public static final String menu_file_run_last_script = Locale.getDefault().equals(Locale.TAIWAN) ? "執行上次腳本" : "Run Last Scripts";
		public static final String menu_file_show_last_result = Locale.getDefault().equals(Locale.TAIWAN) ? "重看上次比較結果" : "Review Last Result";
		public static final String menu_file_clear = Locale.getDefault().equals(Locale.TAIWAN) ? "清除" : "Clear";
		public static final String menu_file_close = Locale.getDefault().equals(Locale.TAIWAN) ? "關閉" : "Close";
		public static final String menu_file_save = Locale.getDefault().equals(Locale.TAIWAN) ? "儲存" : "Save";

		public static final String menu_device_reset_device = Locale.getDefault().equals(Locale.TAIWAN) ? "變更預設裝置" : "Select Default Device";
		public static final String menu_device_reset_apk = Locale.getDefault().equals(Locale.TAIWAN) ? "安裝不同版本Apk" : "Install Another APK";
		public static final String menu_device_reset_package_name = Locale.getDefault().equals(Locale.TAIWAN) ? "變更測試Package Name" : "Change Package Name";
		public static final String menu_device_random_test = Locale.getDefault().equals(Locale.TAIWAN) ? "隨機測試" : "Random Test";
		public static final String menu_device_report_error = Locale.getDefault().equals(Locale.TAIWAN) ? "設定crash report E-mail" : "Set E-mail for crash report.";

		public static final String menu_open_editor = Locale.getDefault().equals(Locale.TAIWAN) ? "開啟編輯器" : "Open Editor";

		public static final String menu_help_tutorial = Locale.getDefault().equals(Locale.TAIWAN) ? "教學" : "Tutorial";
		public static final String menu_help_about = Locale.getDefault().equals(Locale.TAIWAN) ? "關於" : "About";
		public static final String menu_help_screen_shot = Locale.getDefault().equals(Locale.TAIWAN) ? "螢幕截圖" : "ScreenShot";

		public static final String dialog_title_alert = Locale.getDefault().equals(Locale.TAIWAN) ? "注意:" : "Alert:";
		public static final String dialog_title_quikly_choose = Locale.getDefault().equals(Locale.TAIWAN) ? "快速選擇" : "Quick Action";
		public static final String dialog_title_script_select = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇腳本" : "Choose Scripts";
		public static final String dialog_title_choose_sdk = Locale.getDefault().equals(Locale.TAIWAN) ? "請選擇 android-sdk 路徑" : "Set path of android-sdk";
		public static final String dialog_alert_lack_of_sdk = Locale.getDefault().equals(Locale.TAIWAN) ? "缺少或錯誤 android-sdk 路徑，\n請重新設定路徑" : "Error path for android-sdk.\nPlease reset path.";
		public static final String dialog_title_choose_a_device = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇裝置" : "Select a Device.";
		public static final String dialog_alert_choose_a_device = Locale.getDefault().equals(Locale.TAIWAN) ? "請連結手機或是模擬器，並選擇其中一支為預設裝置"
				: "Please confirm that any emulator/device is connected.\n Select a Device.";
		public static final String dialog_alert_no_devices = Locale.getDefault().equals(Locale.TAIWAN) ? "請確認是否已連接任何手機裝置或模擬器" : "Please confirm that any emulator/device is connected.";
		public static final String dialog_alert_find_action_file = Locale.getDefault().equals(Locale.TAIWAN) ? "請選擇從\"monkey recorder模擬器\"中匯出的紀錄檔來產生腳本(副檔名為.mr)"
				: "Please generate a script from a record(.mr).";
		public static final String dialog_alert_file_disappear = Locale.getDefault().equals(Locale.TAIWAN) ? "選擇檔案不存在，請重新確認" : "The File is missing.";
		public static final String dialog_alert_create_a_new_script = Locale.getDefault().equals(Locale.TAIWAN) ? "目前沒有測試腳本，是否產生一份新腳本?" : "No Scripts so far. Wouls you like to generate a new one?";
		public static final String dialog_alert_set_as_target = Locale.getDefault().equals(Locale.TAIWAN) ? "是否設定此圖片為正確結果?" : "Set this picture as a correct one?";
		public static final String dialog_alert_set_as_target_success = Locale.getDefault().equals(Locale.TAIWAN) ? "已成功替換目標" : "Replace Complete.";
		public static final String dialog_alert_save_success = Locale.getDefault().equals(Locale.TAIWAN) ? "儲存成功" : "Save Complete";
		public static final String dialog_alert_input_number = Locale.getDefault().equals(Locale.TAIWAN) ? "請輸入數字" : "Input a number";
		public static final String dialog_alert_input_package_name = Locale.getDefault().equals(Locale.TAIWAN) ? "請輸入Package Name" : "Please input Package Name";
		public static final String dialog_alert_package_name_error = Locale.getDefault().equals(Locale.TAIWAN) ? "Package Name不存在，請重新確認" : "Package Name is not correct.";
		public static final String dialog_alert_save_before_leaving = Locale.getDefault().equals(Locale.TAIWAN) ? "是否儲存？" : "Save change?";
		public static final String dialog_alert_run_last_script = Locale.getDefault().equals(Locale.TAIWAN) ? "執行上次腳本" : "Run the last script";
		public static final String dialog_alert_run_script = Locale.getDefault().equals(Locale.TAIWAN) ? "執行腳本" : "Run the script";
		public static final String dialog_alert_confirm_set_crash_report = Locale.getDefault().equals(Locale.TAIWAN) ? "是否設定crash report" : "Setting crash report?";
		public static final String dialog_title_set_crash_report = Locale.getDefault().equals(Locale.TAIWAN) ? "設定crash report" : "Setting crash report";
		public static final String dialog_alert_input_your_email = Locale.getDefault().equals(Locale.TAIWAN) ? "請輸入你的E-mail" : "Input your E-mail, please.";
		public static final String dialog_alert_input_your_email_password = Locale.getDefault().equals(Locale.TAIWAN) ? "請輸入你的E-mail password" : "Input your E-mail password, please.";
		public static final String dialog_alert_input_random_test_count = Locale.getDefault().equals(Locale.TAIWAN) ? "請輸入隨機亂點次數" : "Input Counts for Random Test Event.";

		public static final String text_target = Locale.getDefault().equals(Locale.TAIWAN) ? "正確截圖" : "Target";
		public static final String text_test = Locale.getDefault().equals(Locale.TAIWAN) ? "對照截圖" : "Test";
		public static final String text_comparison = Locale.getDefault().equals(Locale.TAIWAN) ? "比較結果" : "Result";

		public static final String button_start_from_desktop = Locale.getDefault().equals(Locale.TAIWAN) ? "從桌面開始測試" : "Start from Desktop";
		public static final String button_end_to_desktop = Locale.getDefault().equals(Locale.TAIWAN) ? "結束返回桌面" : "Return to Desktop at the End";
		public static final String button_add_while = Locale.getDefault().equals(Locale.TAIWAN) ? "設定迴圈" : "Set a Loop";
		public static final String button_save = Locale.getDefault().equals(Locale.TAIWAN) ? "儲存腳本(script)" : "Save Script";
		public static final String button_edit_preference = Locale.getDefault().equals(Locale.TAIWAN) ? "編輯資料" : "Edit Data";
		public static final String button_clear_data = Locale.getDefault().equals(Locale.TAIWAN) ? "清除資料" : "Clear Data";
		public static final String button_add_script = Locale.getDefault().equals(Locale.TAIWAN) ? "增加腳本" : "Add a Script";
		public static final String button_remove_last_script = Locale.getDefault().equals(Locale.TAIWAN) ? "取消上一步" : "Cancel the Last Script";
		public static final String button_clear_script = Locale.getDefault().equals(Locale.TAIWAN) ? "清除腳本" : "Clear all";
		public static final String button_run_script = Locale.getDefault().equals(Locale.TAIWAN) ? "執行腳本" : "Run Script";
	}
}
