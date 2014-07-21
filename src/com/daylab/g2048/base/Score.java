package com.daylab.g2048.base;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Score")
public class Score extends Model { 
    @Column(name = "username")
    public String username;
    
    @Column(name = "gridtype")
    public int gridtype;//4*4
    
    @Column(name = "gridvalue")
    public int gridvalue;//2048，朝代
    
    @Column(name = "maxvalue")
    public int maxvalue;//每局到达的最大值
    
    @Column(name = "score")
    public int score;
    
    @Column(name = "move")
    public int move;
    
    @Column(name = "time")
    public long time;
    
    public static List<Score> getAll() {
        return new Select()
            .from(Score.class)
            .orderBy("maxvalue DESC")
            .execute();
    }
}