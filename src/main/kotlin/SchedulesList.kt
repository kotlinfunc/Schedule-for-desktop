import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.schedule.ApairX
import model.schedule.DaysList

class SchedulesList {

    private var groups = mutableStateOf<List<String>>(emptyList())
    private var teachers = mutableStateOf<List<String>>(emptyList())
    private var schedule = mutableStateOf(DaysList(emptyList(), "aaa"))
    var selectList = mutableStateOf(true)


    private val scope = CoroutineScope(Dispatchers.IO)

    @Composable
    fun chooseLists() {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = { selectList.value = true }) {
                Text(
                    text = "Группы",
                    color = Color.Gray
                )
            }

            OutlinedButton(onClick = { selectList.value = false }) {
                Text(
                    text = "Учителя",
                    color = Color.Gray
                )
            }
        }
    }

    @Composable
    @Preview
    fun showLists() {
        Column(modifier = Modifier.background(Color.Gray)) {
            Box {
                chooseLists()
            }
            Box(modifier = Modifier.fillMaxHeight()) {
                if (selectList.value) {
                    groupsColumn()
                } else {
                    teachersColumn()
                }
            }
        }
    }


    @Composable
    fun groupsColumn() {
        scope.launch {
            groups.value = RetrofitInstance.api.getGroupsList().body()?.group!!
        }
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            for (i in groups.value)
                TextButton(onClick = {
                    scope.launch {
                        schedule.value = RetrofitInstance.api.getschedulelist(i).body()!!
                    }
                },
                    modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = i,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
        }
    }

    @Composable
    fun teachersColumn() {
        scope.launch {
            teachers.value = RetrofitInstance.api.getGroupsList().body()?.teacher!!
        }
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            for (i in teachers.value)
                TextButton(onClick = {
                    scope.launch {
                        schedule.value = RetrofitInstance.api.getschedulelist(i).body()!!
                    }
                },
                    modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = i,
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
        }
    }

    @Composable
    fun window() {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth(0.3f)) {
                showLists()
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(10.dp)) {
                daysList(schedule.value)
            }
        }
    }

    @Composable
    fun daysList(schedule: DaysList) {
        for (day in schedule.days)
            Card(modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.LightGray)) {
                    Text(
                        text = day.day,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Column(modifier = Modifier.fillMaxWidth().background(Color.Gray).padding(20.dp)) {
                        day.apairs.forEach { pairsList(it.apair) }
                    }
                }
            }
    }

    @Composable
    fun pairsList(pairs: List<ApairX>) {
        for (pair in pairs) {
            Column {
                Text(text = "${pair.start} - ${pair.end}")
                Text(text = pair.doctrine)
                Text(text = pair.teacher)
                Text(text = pair.auditoria)
                Text(text = pair.corpus)
            }
        }
    }
}

