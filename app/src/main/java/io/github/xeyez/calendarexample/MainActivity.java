package io.github.xeyez.calendarexample;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends GoogleCalendarActivity implements GoogleCalendarActivity.CalendarTaskListener {
    @ViewById
    EditText et_calendarId;

    @ViewById
    EditText et_calendarSummary;

    @ViewById
    EditText et_eventId;

    @ViewById
    EditText et_eventSummary;

    @ViewById
    TextView tv_output;


    @AfterViews
    void onMainActivityAfterViews() {
        setCalendarTaskListener(this);

        tv_output.setVerticalScrollBarEnabled(true);
        tv_output.setMovementMethod(new ScrollingMovementMethod());
    }


    @Click({R.id.btn_calendar_read, R.id.btn_calendar_add, R.id.btn_calendar_update, R.id.btn_calendar_delete,
            R.id.btn_event_read, R.id.btn_event_add, R.id.btn_event_update, R.id.btn_event_delete})
    void myClick(View v) {
        v.setEnabled(false);
        tv_output.setText("");
        getResultsFromApi(v.getId());
        v.setEnabled(true);
    }

    @Override
    public void onAvailableCalendarTask(int viewId) {
        GoogleCalendar observer = GoogleCalendar.build(mCredential);

        String calendarIdText = et_calendarId.getText().toString();
        String calendarSummaryText = et_calendarSummary.getText().toString();

        String eventIdText = et_eventId.getText().toString();
        String eventSummaryText = et_eventSummary.getText().toString();

        switch (viewId) {
            case R.id.btn_calendar_read:
                observer.readCalendar().subscribe(calendarListEntries -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    calendarListEntries.forEach(calendarListEntry -> {
                        stringBuilder.append(calendarListEntry.getId());
                        stringBuilder.append("\n");
                        stringBuilder.append(calendarListEntry.getSummary());
                        stringBuilder.append("\n");
                        stringBuilder.append(calendarListEntry.getDescription());
                        stringBuilder.append("\n\n");

                        tv_output.setText(stringBuilder);
                    });
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_calendar_add:
                observer.addCalendar(calendarSummaryText).subscribe(calendar -> {
                    tv_output.setText(calendar.getId() + " added!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_calendar_update:
                observer.updateCalendar(calendarIdText, calendarSummaryText).subscribe(calendar -> {
                    tv_output.setText(calendar.getId() + " updated!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_calendar_delete:
                observer.deleteCalendar(calendarIdText).subscribe(aVoid -> {
                    tv_output.setText(calendarIdText + " deleted!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;


            case R.id.btn_event_read:
                /*observer.readEventsAll(calendarIdText).subscribe(events -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    events.forEach(event -> {
                        stringBuilder.append(event.getSummary());
                        stringBuilder.append(" / ");
                        stringBuilder.append(event.getStart());
                        stringBuilder.append(" / ");
                        stringBuilder.append(event.getEnd());
                        stringBuilder.append("\n");
                    });

                    tv_output.setText(stringBuilder);
                }, throwable -> handleCommonThrowable(throwable));*/

                observer.readEvents(calendarIdText, 20, true).subscribe(events -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    events.forEach(event -> {
                        stringBuilder.append(event.getSummary());
                        stringBuilder.append(" / ");
                        stringBuilder.append(event.getStart());
                        stringBuilder.append(" / ");
                        stringBuilder.append(event.getEnd());
                        stringBuilder.append("\n");
                    });

                    tv_output.setText(stringBuilder);
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_event_add:
                observer.addEvent(calendarIdText, eventSummaryText, eventSummaryText + " DESCRIPTION").subscribe(event -> {
                    tv_output.setText(event.getId() + " added!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_event_update:
                observer.updateEvent(calendarIdText, eventIdText, eventSummaryText, eventSummaryText + " DESCRIPTION").subscribe(event -> {
                    tv_output.setText(event.getId() + " updated!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;

            case R.id.btn_event_delete:
                observer.deleteEvent(calendarIdText, eventIdText).subscribe(aVoid -> {
                    tv_output.setText(calendarIdText + "." + "eventIdText" + " deleted!!");
                }, throwable -> handleCommonThrowable(throwable));
                break;
        }
    }

    @Override
    public void onErrorCalendarTask(String message) {
        tv_output.setText(message);
    }
}