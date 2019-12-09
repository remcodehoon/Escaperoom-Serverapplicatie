package nl.stokperdje.escaperoom.serverapplication.domain;

import com.google.gson.annotations.Expose;
import nl.stokperdje.escaperoom.serverapplication.dto.Log;
import nl.stokperdje.escaperoom.serverapplication.exceptions.NoActiveSessionException;

import java.util.ArrayList;
import java.util.List;

public class EscaperoomSessie {

    @Expose private String teamName;

    @Expose private int elapsedTime = 0;

    @Expose int hours = 1;
    @Expose int minutes = 0;
    @Expose int seconds = 0;

    @Expose private int buit = 0;

    @Expose private boolean isActive = false;
    @Expose private boolean isStopped = true;

    @Expose private boolean buttonPressed = false;

    @Expose private List<Log> logs = new ArrayList<>();

    public EscaperoomSessie(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public int getElapsedTime() {
        return this.elapsedTime;
    }

    public void countSecond() {
        this.elapsedTime++;
    }

    public void startTimer() {
        this.isStopped = false;
    }

    public void pauseTimer() {
        this.isStopped = true;
    }

    public int getHours() {
        return this.hours;
    }

    public int getMinutes() {
        return this.minutes;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public void setTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public boolean isButtonPressed() {
        return this.buttonPressed;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setStopped(boolean stopped) {
        this.isStopped = stopped;
    }

    public void setBuit(int value) throws NoActiveSessionException {
        if (this.isActive() && !this.isStopped()) {
            this.buit = value;
        } else {
            throw new NoActiveSessionException();
        }
    }

    public int getBuit() {
        return this.buit;
    }

    public void addLog(Log log) {
        this.logs.add(0, log);
    }
}
