package io.github.xeyez.calendarexample;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-05-26.
 */

public class GoogleCalendar {

    private com.google.api.services.calendar.Calendar mService = null;

    private GoogleCalendar(com.google.api.services.calendar.Calendar mService) {
        this.mService = mService;
    }

    public static GoogleCalendar build(GoogleAccountCredential mCredential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        return new GoogleCalendar(mService);
    }

    public Observable<List<CalendarListEntry>> readCalendar() {
        return Observable.create((ObservableOnSubscribe<List<CalendarListEntry>>) e -> {
            CalendarList calendarList = mService.calendarList().list().execute();
            e.onNext(calendarList.getItems());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Calendar> addCalendar(String summary) {
        return Observable.create((ObservableOnSubscribe<Calendar>) e -> {
            Calendar entry = new Calendar().setSummary(summary).setDescription("Test");
            e.onNext(mService.calendars().insert(entry).execute());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Calendar> updateCalendar(String calendarId, String summary) {
        return Observable.create((ObservableOnSubscribe<Calendar>) e -> {
            Calendar entry = new Calendar().setSummary(summary).setDescription("Test");
            e.onNext(mService.calendars().patch(calendarId, entry).execute());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> deleteCalendar(String calendarId) {
        return Observable.create((ObservableOnSubscribe<String>) e -> {
            mService.calendars().delete(calendarId).execute();
            e.onNext(calendarId);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<List<Event>> readEventsAll(String calendarId) {
        return Observable.create((ObservableOnSubscribe<List<Event>>) e -> {
            String tempCalendarId = calendarId == null || calendarId.trim().isEmpty()? "primary" : calendarId;

            Events events = mService.events().list(tempCalendarId).execute();

            e.onNext(events.getItems());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Event>> readEvents(String calendarId, int maxResults, boolean isNow) {
        return Observable.create((ObservableOnSubscribe<List<Event>>) e -> {
            String tempCalendarId = calendarId == null || calendarId.trim().isEmpty()? "primary" : calendarId;

            com.google.api.services.calendar.Calendar.Events.List eventsList = mService.events().list(tempCalendarId).setMaxResults(maxResults);
            if(isNow)
                eventsList = eventsList.setTimeMin(new DateTime(System.currentTimeMillis())).setOrderBy("startTime").setSingleEvents(true);

            e.onNext(eventsList.execute().getItems());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> addEvent(String calendarId, String summary, String description) {
        return Observable.create((ObservableOnSubscribe<Event>) e -> {
            Event event = new Event();
            event.setSummary(summary);
            event.setDescription(description);

            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + 3600000);

            DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
            event.setStart(new EventDateTime().setDateTime(start));

            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
            event.setEnd(new EventDateTime().setDateTime(end));

            e.onNext(mService.events().insert(calendarId, event).execute());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> updateEvent(String calendarId, String eventId, String summary, String description) {
        return Observable.create((ObservableOnSubscribe<Event>) e -> {
            Event event = new Event();
            event.setSummary(summary);
            event.setDescription(description);

            Date startDate = new Date();
            Date endDate = new Date(startDate.getTime() + 3600000);

            DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
            event.setStart(new EventDateTime().setDateTime(start));

            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
            event.setEnd(new EventDateTime().setDateTime(end));

            e.onNext(mService.events().patch(calendarId, eventId, event).execute());
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> deleteEvent(String calendarId, String eventId) {
        return Observable.create((ObservableOnSubscribe<String>) e -> {
            mService.events().delete(calendarId, eventId).execute();
            e.onNext(eventId);
            e.onComplete();
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}