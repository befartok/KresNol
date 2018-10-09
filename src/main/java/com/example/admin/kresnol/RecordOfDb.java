package com.example.admin.kresnol;

/**
 * Created by admin on 08.10.18.
 */

//модель базы данных

public class RecordOfDb {

    private Integer id;
    private String name;
    private Integer totalPlay;
    private Integer totalWin;
    private Integer totalLose;

    RecordOfDb(){}

    public RecordOfDb(Integer id, String name, Integer totalPlay, Integer totalWin, Integer totalLose) {
        this.id = id;
        this.name = name;
        this.totalPlay = totalPlay;
        this.totalWin = totalWin;
        this.totalLose = totalLose;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalPlay() {
        return totalPlay;
    }

    public void setTotalPlay(Integer totalPlay) {
        this.totalPlay = totalPlay;
    }

    public Integer getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Integer totalWin) {
        this.totalWin = totalWin;
    }

    public Integer getTotalLose() {
        return totalLose;
    }

    public void setTotalLose(Integer totalLose) {
        this.totalLose = totalLose;
    }
}