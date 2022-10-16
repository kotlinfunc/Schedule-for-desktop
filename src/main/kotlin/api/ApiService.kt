package api

import model.groupsandteachers.TeachersAndGroups
import model.schedule.DaysList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/v2/schedule/{group}")
     suspend fun getschedulelist(@Path("group") group: String): Response<DaysList>

    @GET("api/v2/schedule/list")
     suspend fun getGroupsList(): Response<TeachersAndGroups>
}