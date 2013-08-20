package com.yulebaby.teacher.net;

import android.content.Context;

import com.yulebaby.teacher.R;

public class Url {

	public static String host_url;

	public static synchronized void initHostUrl(Context context) {
		host_url = context.getString(R.string.host_url);
	}

	public interface Update {
		public final static String version = "/version";
	}

	public interface LoginOut {
		public final static String login = "/login";
		public final static String logout = "/logout";
	}

	public interface Lesson {
		public final static String lesson = "/lesson";
		public final static String lessonUpdate = "/lessonUpdate";
		public final static String reserveLesson = "/reserveLesson";
	}

	public interface Order {
		public final static String orderlist = "/reserve";
		public final static String days = "/reserveDate";
	}

	public interface Interaction {
		public final static String interaction = "/consume";
		public final static String commentView = "/commentView";
	}

	public interface Comment {
		public final static String comment = "/comment";
	}

	public interface AblumList {
		public final static String memberAblum = "/memberAblumList";
		public final static String albumList = "/albumList";
		public final static String search = "/searchAlbumList";
		public final static String postPhoto = "/postMemberAblum";
	}

	public interface Attendance {
		public final static String attendance = "/attendance";
		public final static String months = "/attendanceMonth";
	}

	public interface Salary {
		public final static String payroll = "/payroll";
		public final static String commission = "/payrollCommissionDetail";
		public final static String dayDestail = "/payrollDayDetail";
		public final static String typeDetails = "/payrollDetail";
		public final static String months = "/payrollMonth";
	}

	public interface Exam {
		public final static String examList = "/examList";
		public final static String viewExamPaper = "/viewExamPaper";
		public final static String submitExam = "/postExamPaper";

	}

	public interface SOP {
		public final static String sopLesson = "/sopLesson";
		public final static String loadtype1 = "1";
		public final static String loadtype2 = "2";
		public final static String loadtype3 = "3";
	}
	// public interface Password {
	// public final static String modify = "/modifyPassword";
	// }
	//
	// public interface Order {
	// public final static String days = "/reserveDays";
	// public final static String hours = "/reserveHours";
	// public final static String teachers = "/reserveTeachers";
	// public final static String confirm = "/reserve";
	// public final static String cancel = "/reserveCancel";
	// public final static String list = "/reserveList";
	// }
	//
	// public interface Album {
	// public final static String list = "/albumList";
	// public final static String set = "/albumWidget";
	// }
	//
	// public interface Growth {
	// public final static String list = "/growth";
	// }
	//
	// public interface Swim {
	// public final static String list = "/swimList";
	// public final static String satisfaction = "/swimSatisfaction";
	// public final static String modify = "/swimCase";
	// }
	//
	// public interface Reward {
	// public final static String list = "/point";
	// }
	//
	// public interface Guide {
	// // public final static String title = "/guide";
	// public final static String detail = "/guide";
	// }
	//
	// public interface Message {
	// public final static String feedback = "/feedback";
	// public final static String list = "/messageList";
	// public final static String read = "/messageRead";
	// }
	//
	// public interface Poll {
	// public final static String message_remind = "/messageRemind";
	// }
}
