package edu.np.ece.ame_android_lecturer.Retrofit;

/**
 * Created by MIYA on 31/08/17.
 */
import com.google.gson.JsonObject;

import java.util.List;

import edu.np.ece.ame_android_lecturer.Model.DateOfaLesson;
import edu.np.ece.ame_android_lecturer.Model.ListAttendanceStatus;
import edu.np.ece.ame_android_lecturer.Model.LoginInfo;
import edu.np.ece.ame_android_lecturer.Model.LoginResult;
import edu.np.ece.ame_android_lecturer.Model.StudentInfo;
import edu.np.ece.ame_android_lecturer.Model.TimetableResult;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApi {
    @POST("lecturer/login")
    Call<LoginResult> login(@Body LoginInfo param);

    @GET("user/logout")
    Call<ResponseBody> logout();

    @POST("user/reset-password")
    Call<ResponseBody> resetPassword(@Body JsonObject email);

    @POST("user/change-password")
    Call<ResponseBody> changePassword(@Body JsonObject toUp);

    @POST("beacon-attendance-student/student-attendance")
    Call<String> takeAttendance(@Body JsonObject obj);

    @POST("timetable/get-status")
    Call<String> checkAttendanceStatus(@Body JsonObject obj);

    @GET("lesson-lecturer/weekly-lesson")
    Call<List<TimetableResult>> getTimetableCurrentWeek(@Query("expand") String expand);

    @GET("lesson-date/search")
    Call<List<DateOfaLesson>> getAllDateOfaLesson(@Query("lesson_id") String lesson_id);

    @POST("attendance/list-attendance-status-by-lecturer")
    Call<List<ListAttendanceStatus>> getStudentAttendanceStatus(@Body String lesson_date_id);

    @POST("timetable/get-student")
    Call<List<StudentInfo>> getStudentList(@Body String lesson_id);



}
